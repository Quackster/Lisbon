package net.h4bbo.lisbon.game.player;

import io.netty.util.AttributeKey;
import net.h4bbo.lisbon.dao.mysql.ItemDao;
import net.h4bbo.lisbon.dao.mysql.PlayerDao;
import net.h4bbo.lisbon.dao.mysql.PlayerStatisticsDao;
import net.h4bbo.lisbon.dao.mysql.SettingsDao;
import net.h4bbo.lisbon.game.GameScheduler;
import net.h4bbo.lisbon.game.badges.BadgeManager;
import net.h4bbo.lisbon.game.club.ClubSubscription;
import net.h4bbo.lisbon.game.entity.Entity;
import net.h4bbo.lisbon.game.entity.EntityType;
import net.h4bbo.lisbon.game.fuserights.Fuseright;
import net.h4bbo.lisbon.game.fuserights.FuserightsManager;
import net.h4bbo.lisbon.game.inventory.Inventory;
import net.h4bbo.lisbon.game.messenger.Messenger;
import net.h4bbo.lisbon.game.player.statistics.PlayerStatistic;
import net.h4bbo.lisbon.game.player.statistics.PlayerStatisticManager;
import net.h4bbo.lisbon.game.room.entities.RoomPlayer;
import net.h4bbo.lisbon.messages.outgoing.alert.ALERT;
import net.h4bbo.lisbon.messages.outgoing.alert.HOTEL_LOGOUT;
import net.h4bbo.lisbon.messages.outgoing.club.CLUB_GIFT;
import net.h4bbo.lisbon.messages.outgoing.handshake.AVAILABLE_SETS;
import net.h4bbo.lisbon.messages.outgoing.handshake.LOGIN;
import net.h4bbo.lisbon.messages.outgoing.handshake.RIGHTS;
import net.h4bbo.lisbon.messages.outgoing.moderation.USER_BANNED;
import net.h4bbo.lisbon.messages.outgoing.openinghours.INFO_HOTEL_CLOSING;
import net.h4bbo.lisbon.messages.types.MessageComposer;
import net.h4bbo.lisbon.server.netty.NettyPlayerNetwork;
import net.h4bbo.lisbon.util.DateUtil;
import net.h4bbo.lisbon.util.config.GameConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class Player extends Entity {
    public static final AttributeKey<Player> PLAYER_KEY = AttributeKey.valueOf("Player");

    private final NettyPlayerNetwork network;
    private final PlayerDetails details;
    private final RoomPlayer roomEntity;
    private Set<String> ignoredList;

    private Logger log;
    private Messenger messenger;
    private Inventory inventory;
    private BadgeManager badgeManager;
    private PlayerStatisticManager statisticManager;

    private boolean loggedIn;
    private boolean disconnected;
    private boolean pingOK;
    private int timeConnected;
    private String lastGift;

    public Player(NettyPlayerNetwork nettyPlayerNetwork) {
        this.network = nettyPlayerNetwork;
        this.details = new PlayerDetails();
        this.badgeManager = new BadgeManager();
        this.roomEntity = new RoomPlayer(this);
        this.statisticManager = new PlayerStatisticManager(-1, Map.of());
        this.ignoredList = new HashSet<>();
        this.log = LoggerFactory.getLogger("Connection " + this.network.getConnectionId());
        this.pingOK = true;
        this.disconnected = false;
    }

    /**
     * Login handler for player
     */
    public void login() {
        this.log = LoggerFactory.getLogger("Player " + this.details.getName()); // Update logger to show name
        this.loggedIn = true;
        this.pingOK = true;

        this.timeConnected = DateUtil.getCurrentTimeSeconds();

        PlayerManager.getInstance().disconnectSession(this.details.getId()); // Kill other sessions with same id
        PlayerManager.getInstance().addPlayer(this); // Add new connection

        if (!this.details.getName().equals("Abigail.Ryan")) {
            PlayerDao.saveLastOnline(this.details.getId(), this.details.getLastOnline(), true);
        }

        if (GameConfiguration.getInstance().getBoolean("reset.sso.after.login")) {
            PlayerDao.resetSsoTicket(this.details.getId()); // Protect against replay attacks
        }

        SettingsDao.updateSetting("players.online", String.valueOf(PlayerManager.getInstance().getPlayers().size()));

        this.messenger = new Messenger(this.details);
        this.inventory = new Inventory(this);

        // Bye bye!
        var banned = this.getDetails().isBanned();

        if (banned != null) {
            this.send(new USER_BANNED(banned.getKey()));
            GameScheduler.getInstance().getService().schedule(this::kickFromServer, 1, TimeUnit.SECONDS);
            return;
        }

        /*
        if (this.details.getMachineId() == null || this.details.getMachineId().isBlank() || !(
                this.details.getMachineId().length() == 33 &&
                        this.details.getMachineId().startsWith("#"))) {
            this.details.setMachineId(this.network.getClientMachineId());
            this.network.setSaveMachineId(true);
            PlayerDao.setMachineId(this.details.getId(), this.details.getMachineId());
        }

        if (this.network.saveMachineId()) {
            this.send(new UniqueIDMessageEvent(this.network.getClientMachineId()));
        }

         */
        // Update user IP address
        String ipAddress = NettyPlayerNetwork.getIpAddress(this.getNetwork().getChannel());
        var latestIp = PlayerDao.getLatestIp(this.details.getId());

        if (latestIp == null || !latestIp.equals(ipAddress)) {
            PlayerDao.logIpAddress(this.getDetails().getId(), ipAddress);
        }


        // Set trade ban back to 0, easier for db querying
        if (this.details.getTradeBanExpiration() > 0 && !this.details.isTradeBanned()) {
            this.details.setTradeBanExpiration(0);
            ItemDao.saveTradeBanExpire(this.details.getId(), 0);
        }

        var stats = PlayerStatisticsDao.getStatistics(this.details.getId());

        if (stats.isEmpty()) {
            PlayerStatisticsDao.newStatistics(this.details.getId(), UUID.randomUUID().toString());
            stats = PlayerStatisticsDao.getStatistics(this.details.getId());
        }

        this.statisticManager = new PlayerStatisticManager(this.details.getId(), stats);

        this.badgeManager.loadBadges(this);
        // this.achievementManager.loadAchievements(this.details.getId());
        this.details.resetNextHandout();
        // this.refreshJoinedGroups();

        this.send(new RIGHTS(this.getFuserights()));
        this.send(new LOGIN());

        if (GameConfiguration.getInstance().getBoolean("welcome.message.enabled")) {
            String alertMessage = GameConfiguration.getInstance().getString("welcome.message.content");
            alertMessage = alertMessage.replace("%username%", this.details.getName());

            this.send(new ALERT(alertMessage));
        }

        if (PlayerManager.getInstance().isMaintenance()) {
            this.send(new INFO_HOTEL_CLOSING(PlayerManager.getInstance().getMaintenanceAt()));
        }

        if (ClubSubscription.isGiftDue(this)) {
            this.send(new CLUB_GIFT(1));
        }

        this.messenger.sendStatusUpdate();
    }

    /**
     * Refresh club for player.
     */
    public void refreshClub() {
        if (this.details.hasClubSubscription()) {
            //if (this.getVersion() <= 17) {
            this.send(new AVAILABLE_SETS("[" + GameConfiguration.getInstance().getString("users.figure.parts.club") + "]"));
        }

        if (!this.details.hasClubSubscription()) {
            // If the database still thinks we have Habbo club even after it expired, reset it back to 0.
            if (this.details.getClubExpiration() > 0) {
                this.details.setClubExpiration(0);
                this.send(new RIGHTS(this.getFuserights()));
                //ClubSubscription.resetClothes(this.details);
                PlayerDao.saveSubscription(this.details.getId(), this.details.getFirstClubSubscription(), this.details.getClubExpiration());
            }
        } else {
            ClubSubscription.checkBadges(this);

            if (ClubSubscription.isGiftDue(this)) {
                this.send(new CLUB_GIFT(this.statisticManager.getIntValue(PlayerStatistic.GIFTS_DUE)));
            }
        }

        ClubSubscription.sendHcDays(this);
    }

    /**
     * Send fuseright permissions for player.
     */
    public List<Fuseright> getFuserights() {
        List<Fuseright> fuserights = FuserightsManager.getInstance().getFuserightsForRank(this.details.getRank());

        if (this.getDetails().hasClubSubscription()) {
            fuserights.addAll(FuserightsManager.getInstance().getClubFuserights());
        }

        fuserights.removeIf(fuse -> !fuse.getFuseright().startsWith("fuse_"));
        return fuserights;
    }

    /**
     * Check if the player has a permission for a rank.
     *
     * @param fuse the permission
     * @return true, if successful
     */
    @Override
    public boolean hasFuse(Fuseright fuse) {
        return FuserightsManager.getInstance().hasFuseright(fuse, this.details);
    }

    /**
     * Send a response to the player
     *
     * @param response the response
     */
    public void send(MessageComposer response) {
        this.network.send(response);
    }

    /**
     * Send a object to the player
     *
     * @param object the object to send
     */
    public void sendObject(Object object) {
        this.network.send(object);
    }


    /**
     * Send a queued response to the player
     *
     * @param response the response
     */
    public void sendQueued(MessageComposer response) {
        this.network.sendQueued(response);
    }

    /**
     * Flush queue
     */
    public void flush() {
        this.network.flush();
    }

    /**
     * Get the messenger instance for the player
     *
     * @return the messenger instance
     */
    public Messenger getMessenger() {
        return messenger;
    }

    /**
     * Get the inventory handler for player.
     *
     * @return the inventory handler
     */
    public Inventory getInventory() {
        return inventory;
    }

    /**
     * Get the badge manager for player.
     *
     * @return the badge manager
     */
    public BadgeManager getBadgeManager() {
        return badgeManager;
    }

    @Override
    public PlayerDetails getDetails() {
        return this.details;
    }

    @Override
    public RoomPlayer getRoomUser() {
        return this.roomEntity;
    }

    @Override
    public EntityType getType() {
        return EntityType.PLAYER;
    }

    /**
     * Get the player logger.
     *
     * @return the logger
     */
    public Logger getLogger() {
        return this.log;
    }

    /**
     * Get the network handler for the player
     *
     * @return the network handler
     */
    public NettyPlayerNetwork getNetwork() {
        return this.network;
    }

    /**
     * Get the statistic manager for the user.
     *
     * @return the statistic manager
     */
    public PlayerStatisticManager getStatisticManager() {
        return statisticManager;
    }


    /**
     * Get if the player has logged in or not.
     *
     * @return true, if they have
     */
    public boolean isLoggedIn() {
        return loggedIn;
    }

    /**
     * Get if the connection has timed out or not.
     *
     * @return false, if it hasn't.
     */
    public boolean isPingOK() {
        return pingOK;
    }

    /**
     * Get if the socket has been disconnected.
     *
     * @return true, if it has
     */
    public boolean isDisconnected() {
        return disconnected;
    }

    /**
     * Set if the connection has timed out or not.
     *
     * @param pingOK the value to determine of the connection has timed out
     */
    public void setPingOK(boolean pingOK) {
        this.pingOK = pingOK;
    }

    /**
     * Get rid of the player from the server.
     */
    public void kickFromServer() {
        try {
            this.network.send(new HOTEL_LOGOUT(HOTEL_LOGOUT.LogoutReason.DISCONNECT));
            this.network.disconnect();

            this.dispose();
        } catch (Exception ignored) {
            // Ignore
        }
    }

    /**
     * Dispose player when disconnect happens.
     */
    @Override
    public void dispose() {
        try {
            if (this.loggedIn) {

                if (this.roomEntity.getObservingGameId() != -1) {
                    this.roomEntity.stopObservingGame();
                }

                if (this.roomEntity.getGamePlayer() != null) {
                    this.roomEntity.getGamePlayer().getGame().leaveGame(this.roomEntity.getGamePlayer());
                }

                PlayerManager.getInstance().removePlayer(this);
                ClubSubscription.countMemberDays(this);

                int loggedInTime = (int) (DateUtil.getCurrentTimeSeconds() - this.timeConnected);
                this.statisticManager.incrementValue(PlayerStatistic.ONLINE_TIME, loggedInTime);
                this.details.setLastOnline(DateUtil.getCurrentTimeSeconds());

                if (!this.details.getName().equals("Abigail.Ryan")) {
                    PlayerDao.saveLastOnline(this.details.getId(), this.details.getLastOnline(), false);
                }

                SettingsDao.updateSetting("players.online", String.valueOf(PlayerManager.getInstance().getPlayers().size()));

                if (this.messenger != null) {
                    this.messenger.sendStatusUpdate();
                }

                this.disconnected = true;
                this.loggedIn = false;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public Set<String> getIgnoredList() {
        return ignoredList;
    }

    public void setLastGift(String nextSpriteGift) {
        this.lastGift = nextSpriteGift;
    }

    public String getLastGift() {
        return lastGift;
    }

    /*public int getVersion() {
        return Kepler.getServer().getConnectionRule(this.network.getPort()).getVersion();
    }*/
}

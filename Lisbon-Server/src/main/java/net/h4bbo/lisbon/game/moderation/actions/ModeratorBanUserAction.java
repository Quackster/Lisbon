package net.h4bbo.lisbon.game.moderation.actions;

import net.h4bbo.lisbon.dao.mysql.BanDao;
import net.h4bbo.lisbon.dao.mysql.PlayerDao;
import net.h4bbo.lisbon.game.GameScheduler;
import net.h4bbo.lisbon.game.ban.BanType;
import net.h4bbo.lisbon.game.fuserights.Fuseright;
import net.h4bbo.lisbon.game.moderation.ModerationAction;
import net.h4bbo.lisbon.game.player.Player;
import net.h4bbo.lisbon.game.player.PlayerDetails;
import net.h4bbo.lisbon.game.player.PlayerManager;
import net.h4bbo.lisbon.game.room.Room;
import net.h4bbo.lisbon.messages.outgoing.alert.ALERT;
import net.h4bbo.lisbon.messages.outgoing.moderation.USER_BANNED;
import net.h4bbo.lisbon.server.netty.streams.NettyRequest;
import net.h4bbo.lisbon.util.DateUtil;

import java.util.concurrent.TimeUnit;

public class ModeratorBanUserAction implements ModerationAction {
    @Override
    public void performAction(Player player, Room room, String alertMessage, String notes, NettyRequest reader) {
        if (!player.hasFuse(Fuseright.BAN)) {
            return;
        }

        String name = reader.readString();
        int banHours = reader.readInt();
        boolean banMachineId = reader.readBoolean();
        boolean banIp = reader.readBoolean();

        if (banHours > 100000) {
            banHours = 100000;
        }

        if (banHours < 2) {
            banHours = 2;
        }

        PlayerDetails playerDetails = PlayerManager.getInstance().getPlayerData(name);

        if (playerDetails == null) {
            player.send(new ALERT("Could not find user: " + name));
            return;
        }

        if (playerDetails.isBanned() != null) {
            player.send(new ALERT("User is already banned!"));
            return;
        }

        long banTime = DateUtil.getCurrentTimeSeconds() + TimeUnit.HOURS.toSeconds(banHours);
        BanDao.addBan(BanType.USER_ID, String.valueOf(playerDetails.getId()), banTime, alertMessage, player.getDetails().getId());


        if (banIp) {
            BanDao.addBan(BanType.IP_ADDRESS, PlayerDao.getLatestIp(playerDetails.getId()), banTime, alertMessage, player.getDetails().getId());
        }

        Player target = PlayerManager.getInstance().getPlayerById(playerDetails.getId());

        if (target != null) {
            target.send(new USER_BANNED(alertMessage));
            GameScheduler.getInstance().getService().schedule(target::kickFromServer, 1, TimeUnit.SECONDS);
        }

        player.send(new ALERT("The user " + playerDetails.getName() + " has been banned."));
    }
}

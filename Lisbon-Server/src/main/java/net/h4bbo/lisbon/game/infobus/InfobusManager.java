package net.h4bbo.lisbon.game.infobus;
import net.h4bbo.lisbon.dao.mysql.InfobusDao;
import net.h4bbo.lisbon.game.GameScheduler;
import net.h4bbo.lisbon.game.pathfinder.Position;
import net.h4bbo.lisbon.game.player.Player;
import net.h4bbo.lisbon.game.room.RoomManager;
import net.h4bbo.lisbon.log.Log;
import net.h4bbo.lisbon.messages.outgoing.infobus.CANNOT_ENTER_BUS;
import net.h4bbo.lisbon.messages.outgoing.infobus.POLL_QUESTION;
import net.h4bbo.lisbon.messages.outgoing.infobus.VOTE_RESULTS;
import net.h4bbo.lisbon.messages.outgoing.rooms.items.SHOWPROGRAM;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class InfobusManager {
    private static InfobusManager instance;
    private boolean canUpdateResults;
    private boolean isDoorOpen;
    private boolean isEventActive;
    private InfobusPoll currentPoll;
    private final Map<Position, Position> queueRoute;

    public InfobusManager() {
        this.queueRoute = new HashMap<>();
        this.initialiseQueueRoute();
    }

    public void stopEvent() {
        var room = RoomManager.getInstance().getRoomByModel("park_b");

        if (room != null) {
            for (Player player : room.getEntityManager().getPlayers()) {
                var composer = new CANNOT_ENTER_BUS("The Infobus event has now ended. Please check the site for updates in future.");

                player.send(composer);
                //player.getRoomUser().getPacketQueueAfterRoomLeave().add(composer);

                player.getRoomUser().kick(true);
                //player.getRoomUser().setBeingKicked(false);
            }
        }

        this.updateDoorStatus(false);
        this.currentPoll = null;
    }

    public void updateDoorStatus(boolean doorStatus) {
        this.isDoorOpen = doorStatus;

        var park = RoomManager.getInstance().getRoomByModel("park_a");

        if (park != null) {
            for (var composer : this.getDoorPrograms()) {
                park.send(composer);
            }
        }
    }

    public void sendDoorStatus(Player player) {
        for (var composer : this.getDoorPrograms()) {
            player.send(composer);
        }
    }

    private SHOWPROGRAM[] getDoorPrograms() {
        String state = this.isDoorOpen ? "open" : "close";

        return new SHOWPROGRAM[] {
                new SHOWPROGRAM(new String[] { "bus", state }),
                new SHOWPROGRAM(new String[] { "busDoor", state })
        };
    }

    /**
     * Initiate polling to collect poll data.
     */
    public void startPolling(int pollId) {
        this.canUpdateResults = false;
        this.currentPoll = InfobusDao.get(pollId);

        if (currentPoll == null) {
            return;
        }

        var room = RoomManager.getInstance().getRoomByModel("park_b");

        if (room == null) {
            return;
        }

        for (Player player : room.getEntityManager().getPlayers()) {
            //if (player.getNetwork().isFlashConnected()) {
            //    player.send(new ALERT("Polling has started, unfortunately, flash clients can't vote as it is unsupported by the client."));
            //    continue;
            //}

            if (!InfobusDao.hasAnswer(currentPoll.getId(), player.getDetails().getId())) {
                player.send(new POLL_QUESTION(currentPoll.getPollData().getQuestion(), currentPoll.getPollData().getAnswers()));
            }

        }

        // Polling timer
        /*this.pollRunnable = new FutureRunnable() {
            public void run() {
                try {
                    if (!getDoorStatus() || (currentPoll != null && currentPoll.getPollData().getAnswers().isEmpty())) {
                        cancelFuture();
                        return;
                    }

                    if (pollSeconds.getAndDecrement() == 0) {
                        canUpdateResults = true;
                        showPollResults();
                        cancelFuture();
                    }

                } catch (Exception ex) {
                    Log.getErrorLogger().error("Error occurred in polling: ", ex);
                }
            }
        };

        var future = GameScheduler.getInstance().getService().scheduleAtFixedRate(this.pollRunnable, 0, 1, TimeUnit.SECONDS);
        this.pollRunnable.setFuture(future);*/

        GameScheduler.getInstance().getService().schedule(() -> {
            try {
                showPollResults(currentPoll.getId());
            } catch (Exception ex) {
                Log.getErrorLogger().error("Error occurred in polling: ", ex);
            }
        }, 30, TimeUnit.SECONDS);
    }

    public void showPollResults(int pollId) {
        var currentPoll = InfobusDao.get(pollId);

        if (currentPoll == null) {
            return;
        }

        this.canUpdateResults = true;

        var room = RoomManager.getInstance().getRoomByModel("park_b");

        if (room != null) {
            var answerResults = InfobusDao.getAnswers(currentPoll.getId());
            int totalAnswers = answerResults.values().stream().mapToInt(Integer::intValue).sum();

            room.send(new VOTE_RESULTS(currentPoll.getPollData().getQuestion(), currentPoll.getPollData().getAnswers(), answerResults, totalAnswers));
        }
    }

    public boolean isDoorOpen() {
        return isDoorOpen;
    }

    public void setDoorOpen(boolean doorOpen) {
        isDoorOpen = doorOpen;
    }

    public boolean isEventActive() {
        return isEventActive;
    }

    public void setEventActive(boolean eventActive) {
        isEventActive = eventActive;
    }

    public InfobusPoll getCurrentPoll() {
        return currentPoll;
    }

    /**
     * Get the infobus manager instance.
     *
     * @return the infobus manager
     */
    public static InfobusManager getInstance() {
        if (instance == null) {
            instance = new InfobusManager();
        }

        return instance;
    }

    public boolean canUpdateResults() {
        return canUpdateResults;
    }

    public int getDoorX() {
        return 28;
    }

    public int getDoorY() {
        return 4;
    }

    public int getQueueStartX() {
        return 19;
    }

    public int getQueueStartY() {
        return 6;
    }

    public Position getNextQueueTile(Position currentPosition) {
        return this.queueRoute.get(currentPosition);
    }

    private void initialiseQueueRoute() {
        this.queueRoute.put(new Position(19, 6), new Position(20, 6));
        this.queueRoute.put(new Position(20, 6), new Position(21, 6));
        this.queueRoute.put(new Position(21, 6), new Position(22, 6));
        this.queueRoute.put(new Position(22, 6), new Position(23, 6));
        this.queueRoute.put(new Position(23, 6), new Position(24, 6));
        this.queueRoute.put(new Position(24, 6), new Position(25, 6));
        this.queueRoute.put(new Position(25, 6), new Position(26, 6));
        this.queueRoute.put(new Position(26, 6), new Position(26, 7));
        this.queueRoute.put(new Position(26, 7), new Position(26, 8));
        this.queueRoute.put(new Position(26, 8), new Position(26, 9));
        this.queueRoute.put(new Position(26, 9), new Position(26, 10));
        this.queueRoute.put(new Position(26, 10), new Position(26, 11));
        this.queueRoute.put(new Position(26, 11), new Position(26, 12));
        this.queueRoute.put(new Position(26, 12), new Position(27, 12));
        this.queueRoute.put(new Position(27, 12), new Position(28, 12));
        this.queueRoute.put(new Position(28, 12), new Position(28, 11));
        this.queueRoute.put(new Position(28, 11), new Position(28, 10));
        this.queueRoute.put(new Position(28, 10), new Position(28, 9));
        this.queueRoute.put(new Position(28, 9), new Position(28, 8));
        this.queueRoute.put(new Position(28, 8), new Position(28, 7));
        this.queueRoute.put(new Position(28, 7), new Position(28, 6));
        this.queueRoute.put(new Position(28, 6), new Position(28, 5));
        this.queueRoute.put(new Position(28, 5), new Position(28, 4));
    }

}

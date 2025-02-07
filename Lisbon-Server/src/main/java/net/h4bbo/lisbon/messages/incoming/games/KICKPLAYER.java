package net.h4bbo.lisbon.messages.incoming.games;

import net.h4bbo.lisbon.game.games.Game;
import net.h4bbo.lisbon.game.games.GameManager;
import net.h4bbo.lisbon.game.games.player.GamePlayer;
import net.h4bbo.lisbon.game.games.player.GameTeam;
import net.h4bbo.lisbon.game.player.Player;
import net.h4bbo.lisbon.game.room.Room;
import net.h4bbo.lisbon.game.triggers.GameLobbyTrigger;
import net.h4bbo.lisbon.messages.outgoing.games.CREATEFAILED;
import net.h4bbo.lisbon.messages.types.MessageEvent;
import net.h4bbo.lisbon.server.netty.streams.NettyRequest;

public class KICKPLAYER implements MessageEvent {
    @Override
    public void handle(Player player, NettyRequest reader) throws Exception {
        if (player.getRoomUser().getRoom() == null) {
            return;
        }

        Room room = player.getRoomUser().getRoom();

        if (!(room.getModel().getRoomTrigger() instanceof GameLobbyTrigger)) {
            return;
        }

        GamePlayer gamePlayer = player.getRoomUser().getGamePlayer();

        if (gamePlayer == null) {
            return;
        }

        Game game = GameManager.getInstance().getGameById(gamePlayer.getGameId());

        if (game == null || game.getGameCreatorId() != player.getDetails().getId()) {
            return;
        }

        int instanceId = reader.readInt();

        GamePlayer teamPlayer = null;

        for (GameTeam team : game.getTeams().values()) {
            for (GamePlayer p : team.getActivePlayers()) {
                if (p.getPlayer().getRoomUser().getInstanceId() == instanceId) {
                    teamPlayer = p;
                    break;
                }
            }
        }

        if (teamPlayer == null) {
            return;
        }

        game.leaveGame(teamPlayer);
        teamPlayer.getPlayer().send(new CREATEFAILED(CREATEFAILED.FailedReason.KICKED));
    }
}

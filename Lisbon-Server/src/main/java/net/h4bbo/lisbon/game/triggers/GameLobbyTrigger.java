package net.h4bbo.lisbon.game.triggers;

import net.h4bbo.lisbon.game.entity.Entity;
import net.h4bbo.lisbon.game.games.GameManager;
import net.h4bbo.lisbon.game.games.enums.GameType;
import net.h4bbo.lisbon.game.player.Player;
import net.h4bbo.lisbon.game.room.Room;
import net.h4bbo.lisbon.messages.outgoing.games.GAMEPLAYERINFO;
import net.h4bbo.lisbon.messages.outgoing.games.INSTANCELIST;
import net.h4bbo.lisbon.messages.types.MessageComposer;

import java.util.List;
import java.util.Map;

public abstract class GameLobbyTrigger extends GenericTrigger {
    public void onRoomEntry(Entity entity, Room room, Object... customArgs) { }
    public void onRoomLeave(Entity entity, Room room, Object... customArgs) { }

    public void showPoints(Player player, Room room) {
        room.send(new GAMEPLAYERINFO(this.getGameType(), List.of(player)));
    }

    public abstract void createGame(Player gameCreator, Map<String, Object> gameParameters);
    public abstract GameType getGameType();

    public MessageComposer getInstanceList() {
        return new INSTANCELIST(GameManager.getInstance().getGamesByType(this.getGameType()), GameManager.getInstance().getLastPlayedGames(this.getGameType()));
    }
}

package net.h4bbo.lisbon.game.games.battleball.events;

import net.h4bbo.lisbon.game.games.GameEvent;
import net.h4bbo.lisbon.game.games.enums.GameEventType;
import net.h4bbo.lisbon.game.games.player.GamePlayer;
import net.h4bbo.lisbon.game.pathfinder.Position;
import net.h4bbo.lisbon.server.netty.streams.NettyResponse;

public class PlayerMoveEvent extends GameEvent {
    private final GamePlayer gamePlayer;
    private final Position nextPosition;

    public PlayerMoveEvent(GamePlayer gamePlayer, Position nextPosition) {
        super(GameEventType.BATTLEBALL_PLAYER_EVENT);
        this.gamePlayer = gamePlayer;
        this.nextPosition = nextPosition;
    }

    @Override
    public void serialiseEvent(NettyResponse response) {
        response.writeInt(this.gamePlayer.getPlayer().getRoomUser().getInstanceId());
        response.writeInt(this.nextPosition.getX());
        response.writeInt(this.nextPosition.getY());
    }

    public GamePlayer getGamePlayer() {
        return gamePlayer;
    }
}

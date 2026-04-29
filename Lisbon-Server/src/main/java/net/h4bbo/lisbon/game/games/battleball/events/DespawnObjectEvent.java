package net.h4bbo.lisbon.game.games.battleball.events;

import net.h4bbo.lisbon.game.games.GameEvent;
import net.h4bbo.lisbon.game.games.enums.GameEventType;
import net.h4bbo.lisbon.server.netty.streams.NettyResponse;

public class DespawnObjectEvent extends GameEvent {
    private final int gameObjectId;

    public DespawnObjectEvent(int gameObjectId) {
        super(GameEventType.BATTLEBALL_DESPAWN_OBJECT);
        this.gameObjectId = gameObjectId;
    }

    @Override
    public void serialiseEvent(NettyResponse response) {
        response.writeInt(this.gameObjectId);
    }
}

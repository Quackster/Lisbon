package net.h4bbo.lisbon.game.games.battleball.events;

import net.h4bbo.lisbon.game.games.GameEvent;
import net.h4bbo.lisbon.game.games.battleball.objects.PinObject;
import net.h4bbo.lisbon.game.games.enums.GameEventType;
import net.h4bbo.lisbon.game.pathfinder.Position;
import net.h4bbo.lisbon.server.netty.streams.NettyResponse;

public class PinSpawnEvent extends GameEvent {
    private final int id;
    private final Position position;

    public PinSpawnEvent(int id, Position position) {
        super(GameEventType.BATTLEBALL_OBJECT_SPAWN);
        this.id = id;
        this.position = position;
    }

    @Override
    public void serialiseEvent(NettyResponse response) {
        response.writeInt(2);
        new PinObject(this.id, this.position).serialiseObject(response);
    }
}

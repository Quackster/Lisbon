package net.h4bbo.lisbon.game.games.snowstorm.events;

import net.h4bbo.lisbon.game.games.GameObject;
import net.h4bbo.lisbon.game.games.enums.GameObjectType;
import net.h4bbo.lisbon.server.netty.streams.NettyResponse;

public class SnowStormMachineAddSnowballEvent extends GameObject {
    private final int machineId;

    public SnowStormMachineAddSnowballEvent(int machineId) {
        super(machineId, GameObjectType.SNOWWAR_MACHINE_ADD_SNOWBALL_EVENT);
        this.machineId = machineId;
    }

    @Override
    public void serialiseObject(NettyResponse response) {
        response.writeInt(this.getGameObjectType().getObjectId());
        response.writeInt(this.machineId);
    }
}

package net.h4bbo.lisbon.game.games.battleball.objects;

import net.h4bbo.lisbon.game.games.GameObject;
import net.h4bbo.lisbon.game.games.battleball.BattleBallPowerUp;
import net.h4bbo.lisbon.game.games.enums.GameObjectType;
import net.h4bbo.lisbon.server.netty.streams.NettyResponse;

public class PowerUpUpdateObject extends GameObject {
    private final BattleBallPowerUp powerUp;

    public PowerUpUpdateObject(BattleBallPowerUp powerUp) {
        super(powerUp.getId(), GameObjectType.BATTLEBALL_POWER_OBJECT);
        this.powerUp = powerUp;
    }

    @Override
    public void serialiseObject(NettyResponse response) {
        response.writeInt(this.powerUp.getId());
        response.writeInt(this.powerUp.getTimeToDespawn().get());
        response.writeInt(this.powerUp.getPlayerHolding());
    }
}

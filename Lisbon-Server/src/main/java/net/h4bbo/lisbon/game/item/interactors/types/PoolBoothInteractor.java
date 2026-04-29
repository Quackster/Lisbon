package net.h4bbo.lisbon.game.item.interactors.types;

import net.h4bbo.lisbon.game.entity.Entity;
import net.h4bbo.lisbon.game.entity.EntityType;
import net.h4bbo.lisbon.game.item.Item;
import net.h4bbo.lisbon.game.player.Player;
import net.h4bbo.lisbon.game.room.entities.RoomEntity;
import net.h4bbo.lisbon.game.triggers.GenericTrigger;
import net.h4bbo.lisbon.messages.outgoing.rooms.pool.OPEN_UIMAKOPPI;

public class PoolBoothInteractor extends GenericTrigger {
    @Override
    public void onEntityStop(Entity entity, RoomEntity roomEntity, Item item, boolean isRotation) {
        if (entity.getType() != EntityType.PLAYER) {
            return;
        }

        Player player = (Player)entity;

        player.getRoomUser().setWalkingAllowed(false);
        player.getRoomUser().getTimerManager().resetRoomTimer(120); // Only allow 120 seconds when changing clothes, to stop someone from just afking in the booth for 15 minutes.
        player.send(new OPEN_UIMAKOPPI());

        item.showProgram("close");
    }

    @Override
    public void onEntityLeave(Entity entity, RoomEntity roomEntity, Item item) {
        item.showProgram("open");
    }
}

package net.h4bbo.lisbon.game.item.interactors.types.wobblesquabble;

import net.h4bbo.lisbon.game.entity.Entity;
import net.h4bbo.lisbon.game.entity.EntityType;
import net.h4bbo.lisbon.game.games.wobblesquabble.WobbleSquabbleManager;
import net.h4bbo.lisbon.game.item.Item;
import net.h4bbo.lisbon.game.room.entities.RoomEntity;
import net.h4bbo.lisbon.game.triggers.GenericTrigger;

public class WobbleSquabbleQueueTile extends GenericTrigger {
    @Override
    public void onEntityStop(Entity entity, RoomEntity roomEntity, Item item, boolean isRotation) {
        if (entity.getType() != EntityType.PLAYER) {
            return;
        }

        if (roomEntity.getRoom().getTaskManager().hasTask(WobbleSquabbleManager.getInstance().getName())) {
            return;
        }

        roomEntity.walkTo(roomEntity.getPosition().getSquareInFront().getX(), roomEntity.getPosition().getSquareInFront().getY());
    }
}

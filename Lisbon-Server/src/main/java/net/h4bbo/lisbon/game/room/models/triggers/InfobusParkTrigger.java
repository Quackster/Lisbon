package net.h4bbo.lisbon.game.room.models.triggers;

import net.h4bbo.lisbon.game.entity.Entity;
import net.h4bbo.lisbon.game.entity.EntityType;
import net.h4bbo.lisbon.game.infobus.InfobusManager;
import net.h4bbo.lisbon.game.player.Player;
import net.h4bbo.lisbon.game.room.Room;
import net.h4bbo.lisbon.game.triggers.GenericTrigger;

public class InfobusParkTrigger extends GenericTrigger {
    @Override
    public void onRoomEntry(Entity entity, Room room, boolean firstEntry, Object... customArgs) {
        if (entity.getType() != EntityType.PLAYER) {
            return;
        }

        Player player = (Player) entity;
        InfobusManager.getInstance().sendDoorStatus(player);

        /*
        List<MessageComposer> messageComposers = new ArrayList<>();
        player.getRoomUser().getPacketQueueAfterRoomLeave().drainTo(messageComposers);

        for (var composer : messageComposers) {
            player.send(composer);
        }*/
    }

    @Override
    public void onRoomLeave(Entity entity, Room room, Object... customArgs) {

    }
}

package net.h4bbo.lisbon.messages.incoming.rooms;

import net.h4bbo.lisbon.game.item.interactors.InteractionType;
import net.h4bbo.lisbon.game.player.Player;
import net.h4bbo.lisbon.game.room.Room;
import net.h4bbo.lisbon.messages.outgoing.rooms.ACTIVE_OBJECTS;
import net.h4bbo.lisbon.messages.outgoing.rooms.OBJECTS_WORLD;
import net.h4bbo.lisbon.messages.types.MessageEvent;
import net.h4bbo.lisbon.server.netty.streams.NettyRequest;

public class G_OBJS implements MessageEvent {
    @Override
    public void handle(Player player, NettyRequest reader) {
        if (player.getRoomUser().getRoom() == null) {
            return;
        }

        Room room = player.getRoomUser().getRoom();

        player.sendQueued(new OBJECTS_WORLD(room.getItemManager().getPublicItems()));

        // Weird behaviour, can't interact upon load, see FlatTrigger as workaround
        player.sendQueued(new ACTIVE_OBJECTS(room.getItemManager().getFloorItems().stream()
                .filter(x -> x.getDefinition().getInteractionType() != InteractionType.PET_WATER_BOWL).toList()));

        player.flush();

        player.getMessenger().sendStatusUpdate();
    }
}

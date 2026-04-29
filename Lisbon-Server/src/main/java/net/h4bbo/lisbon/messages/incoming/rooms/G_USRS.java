package net.h4bbo.lisbon.messages.incoming.rooms;

import net.h4bbo.lisbon.game.player.Player;
import net.h4bbo.lisbon.game.room.Room;
import net.h4bbo.lisbon.messages.outgoing.rooms.user.USER_OBJECTS;
import net.h4bbo.lisbon.messages.types.MessageEvent;
import net.h4bbo.lisbon.server.netty.streams.NettyRequest;

public class G_USRS implements MessageEvent {
    @Override
    public void handle(Player player, NettyRequest reader) {
        if (player.getRoomUser().getRoom() == null) {
            return;
        }

        if (player.getRoomUser().getGamePlayer() != null && player.getRoomUser().getGamePlayer().isInGame()) {
            return; // Not needed for game arenas
        }

        Room room = player.getRoomUser().getRoom();
        player.send(new USER_OBJECTS(room.getEntities()));
    }
}

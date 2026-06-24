package net.h4bbo.lisbon.messages.incoming.rooms.settings;

import net.h4bbo.lisbon.game.player.Player;
import net.h4bbo.lisbon.game.room.Room;
import net.h4bbo.lisbon.game.room.RoomManager;
import net.h4bbo.lisbon.messages.outgoing.navigator.RECOMMENDED_ROOM_LIST;
import net.h4bbo.lisbon.messages.outgoing.rooms.settings.FLATINFO;
import net.h4bbo.lisbon.messages.types.MessageEvent;
import net.h4bbo.lisbon.server.netty.streams.NettyRequest;

import java.util.Collections;

public class GETFLATINFO implements MessageEvent {
    @Override
    public void handle(Player player, NettyRequest reader) {
        int roomId = Integer.parseInt(reader.contents());

        Room room = RoomManager.getInstance().getRoomById(roomId);

        if (room == null) {
            return;
        }

        player.send(new RECOMMENDED_ROOM_LIST(player, Collections.emptyList()));
        player.send(new FLATINFO(player, room));
    }
}

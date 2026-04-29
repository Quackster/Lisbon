package net.h4bbo.lisbon.messages.incoming.rooms.settings;

import net.h4bbo.lisbon.game.player.Player;
import net.h4bbo.lisbon.game.room.Room;
import net.h4bbo.lisbon.game.room.RoomManager;
import net.h4bbo.lisbon.messages.outgoing.rooms.settings.FLATCAT;
import net.h4bbo.lisbon.messages.types.MessageEvent;
import net.h4bbo.lisbon.server.netty.streams.NettyRequest;

public class GETFLATCAT implements MessageEvent {
    @Override
    public void handle(Player player, NettyRequest reader) {
        int roomId = reader.readInt();

        Room room = RoomManager.getInstance().getRoomById(roomId);

        if (room == null) {
            return;
        }

        player.send(new FLATCAT(room.getId(), room.getData().getCategoryId()));
    }
}

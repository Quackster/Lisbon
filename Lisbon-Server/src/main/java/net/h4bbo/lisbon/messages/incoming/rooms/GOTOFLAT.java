package net.h4bbo.lisbon.messages.incoming.rooms;

import net.h4bbo.lisbon.game.player.Player;
import net.h4bbo.lisbon.game.room.Room;
import net.h4bbo.lisbon.game.room.RoomManager;
import net.h4bbo.lisbon.messages.types.MessageEvent;
import net.h4bbo.lisbon.server.netty.streams.NettyRequest;

public class GOTOFLAT implements MessageEvent {
    @Override
    public void handle(Player player, NettyRequest reader) {
        int roomId = Integer.parseInt(reader.contents());

        if (player.getRoomUser().getAuthenticateId() != roomId) {
            return;
        }

        Room room = RoomManager.getInstance().getRoomById(roomId);

        if (room == null) {
            return;
        }

        room.getEntityManager().enterRoom(player, null);
    }
}

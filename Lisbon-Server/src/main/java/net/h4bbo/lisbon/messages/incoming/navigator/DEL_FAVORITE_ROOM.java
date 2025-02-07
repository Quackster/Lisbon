package net.h4bbo.lisbon.messages.incoming.navigator;

import net.h4bbo.lisbon.dao.mysql.RoomFavouritesDao;
import net.h4bbo.lisbon.game.player.Player;
import net.h4bbo.lisbon.game.room.RoomManager;
import net.h4bbo.lisbon.messages.types.MessageEvent;
import net.h4bbo.lisbon.server.netty.streams.NettyRequest;

public class DEL_FAVORITE_ROOM implements MessageEvent {
    @Override
    public void handle(Player player, NettyRequest reader) throws Exception {
        int roomType = reader.readInt();
        int roomId = reader.readInt();

        if (roomType == 1) {
            roomId = (roomId - RoomManager.PUBLIC_ROOM_OFFSET);
        }

        RoomFavouritesDao.removeFavouriteRoom(player.getDetails().getId(), roomId);
    }
}

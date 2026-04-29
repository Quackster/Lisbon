package net.h4bbo.lisbon.messages.incoming.rooms.settings;

import net.h4bbo.lisbon.dao.mysql.RoomDao;
import net.h4bbo.lisbon.game.entity.Entity;
import net.h4bbo.lisbon.game.player.Player;
import net.h4bbo.lisbon.game.room.Room;
import net.h4bbo.lisbon.game.room.RoomManager;
import net.h4bbo.lisbon.log.Log;
import net.h4bbo.lisbon.messages.types.MessageEvent;
import net.h4bbo.lisbon.server.netty.streams.NettyRequest;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DELETEFLAT implements MessageEvent {
    @Override
    public void handle(Player player, NettyRequest reader) throws SQLException {
        int roomId = Integer.parseInt(reader.contents());
        delete(roomId, player.getDetails().getId());
    }

    public static void delete(int roomId, int userId) throws SQLException {
        Room room = RoomManager.getInstance().getRoomById(roomId);

        if (room == null) {
            return;
        }

        if (!room.isOwner(userId)) {
            return;
        }

        for (var item : room.getItems()) {
            item.delete();
        }

        List<Entity> entities = new ArrayList<>(room.getEntities());

        for (Entity entity : entities) {
            room.getEntityManager().leaveRoom(entity, true);
        }

        if (!room.tryDispose()) {
            Log.getErrorLogger().error("Room " + roomId + " did not want to get disposed by player id " + userId);
        }

        RoomDao.delete(room);
    }
}

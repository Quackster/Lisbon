package net.h4bbo.lisbon.messages.incoming.rooms.moderation;

import net.h4bbo.lisbon.dao.mysql.RoomRightsDao;
import net.h4bbo.lisbon.game.fuserights.Fuseright;
import net.h4bbo.lisbon.game.player.Player;
import net.h4bbo.lisbon.game.room.Room;
import net.h4bbo.lisbon.game.room.RoomManager;
import net.h4bbo.lisbon.messages.types.MessageEvent;
import net.h4bbo.lisbon.server.netty.streams.NettyRequest;

public class REMOVEALLRIGHTS implements MessageEvent {
    @Override
    public void handle(Player player, NettyRequest reader) {
        int roomId = reader.readInt();

        Room room = RoomManager.getInstance().getRoomById(roomId);

        if (room == null) {
            return;
        }

        if (!room.isOwner(player.getDetails().getId()) && !player.hasFuse(Fuseright.ANY_ROOM_CONTROLLER)) {
            return;
        }

        room.getRights().clear();

        for (Player roomPlayer : room.getEntityManager().getPlayers()) {
            room.refreshRights(roomPlayer);
        }

        RoomRightsDao.deleteRoomRights(room.getData());
    }
}

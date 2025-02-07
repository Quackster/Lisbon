package net.h4bbo.lisbon.messages.incoming.rooms.settings;

import net.h4bbo.lisbon.dao.mysql.RoomDao;
import net.h4bbo.lisbon.game.navigator.NavigatorCategory;
import net.h4bbo.lisbon.game.navigator.NavigatorManager;
import net.h4bbo.lisbon.game.player.Player;
import net.h4bbo.lisbon.game.room.Room;
import net.h4bbo.lisbon.game.room.RoomManager;
import net.h4bbo.lisbon.messages.types.MessageEvent;
import net.h4bbo.lisbon.server.netty.streams.NettyRequest;

public class SETFLATCAT implements MessageEvent {
    @Override
    public void handle(Player player, NettyRequest reader) {
        int roomId = reader.readInt();
        int categoryId = reader.readInt();

        NavigatorCategory category = NavigatorManager.getInstance().getCategoryById(categoryId);

        if (category == null) {
            return;
        }

        if (category.getMinimumRoleSetFlat().getRankId() > player.getDetails().getRank().getRankId()) {
            return;
        }

        if (category.isNode() || category.isPublicSpaces()) {
            category = NavigatorManager.getInstance().getCategoryById(2); // No category
        }

        Room room = RoomManager.getInstance().getRoomById(roomId);

        if (room == null) {
            return;
        }

        if (!room.isOwner(player.getDetails().getId())) {
            return;
        }

        room.getData().setCategoryId(category.getId());
        RoomDao.save(room);
    }
}

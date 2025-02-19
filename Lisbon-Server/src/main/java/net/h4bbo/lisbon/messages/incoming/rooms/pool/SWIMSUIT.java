package net.h4bbo.lisbon.messages.incoming.rooms.pool;

import net.h4bbo.lisbon.dao.mysql.PlayerDao;
import net.h4bbo.lisbon.game.player.Player;
import net.h4bbo.lisbon.game.room.Room;
import net.h4bbo.lisbon.game.room.public_rooms.PoolHandler;
import net.h4bbo.lisbon.messages.outgoing.rooms.user.USER_OBJECTS;
import net.h4bbo.lisbon.messages.types.MessageEvent;
import net.h4bbo.lisbon.server.netty.streams.NettyRequest;
import net.h4bbo.lisbon.util.StringUtil;

public class SWIMSUIT implements MessageEvent {
    @Override
    public void handle(Player player, NettyRequest reader) {
        Room room = player.getRoomUser().getRoom();

        if (room == null) {
            return;
        }

        if (!room.getData().getModel().equals("pool_a") &&
                !room.getData().getModel().equals("md_a")) {
            return;
        }

        String swimsuit = StringUtil.filterInput(reader.contents(), true);
        player.getDetails().setPoolFigure(swimsuit);

        room.send(new USER_OBJECTS(player));

        PlayerDao.saveDetails(
                player.getDetails().getId(),
                player.getDetails().getFigure(),
                player.getDetails().getPoolFigure(),
                player.getDetails().getSex());
    }
}

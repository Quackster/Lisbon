package net.h4bbo.lisbon.messages.incoming.rooms.user;

import net.h4bbo.lisbon.dao.mysql.TagDao;
import net.h4bbo.lisbon.game.player.Player;
import net.h4bbo.lisbon.game.player.PlayerManager;
import net.h4bbo.lisbon.game.room.Room;
import net.h4bbo.lisbon.messages.outgoing.rooms.user.TAG_LIST;
import net.h4bbo.lisbon.messages.types.MessageEvent;
import net.h4bbo.lisbon.server.netty.streams.NettyRequest;

public class GET_USER_TAGS implements MessageEvent {
    @Override
    public void handle(Player player, NettyRequest reader) throws Exception {
        Room room = player.getRoomUser().getRoom();

        if (room == null) {
            return;
        }

        Player p = PlayerManager.getInstance().getPlayerById(reader.readInt());

        if (p == null) {
            return;
        }

        player.send(new TAG_LIST(p.getDetails().getId(), TagDao.getUserTags(p.getDetails().getId())));
    }
}

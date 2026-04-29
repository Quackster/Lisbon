package net.h4bbo.lisbon.messages.incoming.user;

import net.h4bbo.lisbon.dao.mysql.PlayerDao;
import net.h4bbo.lisbon.dao.mysql.UsersMutesDao;
import net.h4bbo.lisbon.game.player.Player;
import net.h4bbo.lisbon.messages.outgoing.user.IGNORE_USER_RESULT;
import net.h4bbo.lisbon.messages.types.MessageEvent;
import net.h4bbo.lisbon.server.netty.streams.NettyRequest;

public class IGNORE_USER implements MessageEvent {
    @Override
    public void handle(Player player, NettyRequest reader) throws Exception {
        String username = reader.readString();

        if (player.getIgnoredList().contains(username)) {
            return;
        }

        int userId = PlayerDao.getId(username);
        UsersMutesDao.addMuted(player.getDetails().getId(), userId);

        player.getIgnoredList().add(username);
        player.send(new IGNORE_USER_RESULT(1));
    }
}

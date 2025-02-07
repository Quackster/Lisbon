package net.h4bbo.lisbon.messages.incoming.recycler;

import net.h4bbo.lisbon.dao.mysql.RecyclerDao;
import net.h4bbo.lisbon.game.player.Player;
import net.h4bbo.lisbon.game.recycler.RecyclerManager;
import net.h4bbo.lisbon.messages.outgoing.recycler.RECYCLER_STATUS;
import net.h4bbo.lisbon.messages.types.MessageEvent;
import net.h4bbo.lisbon.server.netty.streams.NettyRequest;

public class GET_FURNI_RECYCLER_STATUS implements MessageEvent {
    @Override
    public void handle(Player player, NettyRequest reader) throws Exception {
        player.send(new RECYCLER_STATUS(
                RecyclerManager.getInstance().isRecyclerEnabled(),
                RecyclerDao.getSession(player.getDetails().getId())));
    }
}

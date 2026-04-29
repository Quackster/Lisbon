package net.h4bbo.lisbon.messages.incoming.register;

import net.h4bbo.lisbon.game.player.Player;
import net.h4bbo.lisbon.messages.outgoing.register.DATE;
import net.h4bbo.lisbon.messages.types.MessageEvent;
import net.h4bbo.lisbon.server.netty.streams.NettyRequest;
import net.h4bbo.lisbon.util.DateUtil;

public class GDATE implements MessageEvent {
    @Override
    public void handle(Player player, NettyRequest reader) throws Exception {
        player.send(new DATE(DateUtil.getShortDate().replace("-", ".")));
    }
}

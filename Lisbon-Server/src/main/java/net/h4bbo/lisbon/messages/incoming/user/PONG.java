package net.h4bbo.lisbon.messages.incoming.user;

import net.h4bbo.lisbon.game.player.Player;
import net.h4bbo.lisbon.messages.types.MessageEvent;
import net.h4bbo.lisbon.server.netty.streams.NettyRequest;

public class PONG implements MessageEvent {
    @Override
    public void handle(Player player, NettyRequest reader) {
        // Nice pong :^)
        player.setPingOK(true);
    }
}

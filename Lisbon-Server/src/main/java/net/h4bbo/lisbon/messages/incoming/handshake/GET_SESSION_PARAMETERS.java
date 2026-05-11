package net.h4bbo.lisbon.messages.incoming.handshake;

import net.h4bbo.lisbon.game.player.Player;
import net.h4bbo.lisbon.messages.outgoing.handshake.SESSION_PARAMETERS;
import net.h4bbo.lisbon.messages.types.MessageEvent;
import net.h4bbo.lisbon.server.netty.streams.NettyRequest;

public class GET_SESSION_PARAMETERS implements MessageEvent {

    @Override
    public void handle(Player player, NettyRequest reader) {
        if (player.isLoggedIn()) {
            return;
        }

        player.send(new SESSION_PARAMETERS(player.getDetails()));
    }
}

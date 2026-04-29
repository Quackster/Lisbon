package net.h4bbo.lisbon.messages.incoming.register;

import net.h4bbo.lisbon.game.player.Player;
import net.h4bbo.lisbon.messages.outgoing.register.EMAIL_APPROVED;
import net.h4bbo.lisbon.messages.types.MessageEvent;
import net.h4bbo.lisbon.server.netty.streams.NettyRequest;

public class APPROVEEMAIL implements MessageEvent {
    @Override
    public void handle(Player player, NettyRequest reader) throws Exception {
        if (player.isLoggedIn()) {
            return;
        }

        String email = reader.readString();

        player.send(new EMAIL_APPROVED());
    }
}

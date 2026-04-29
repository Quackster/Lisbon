package net.h4bbo.lisbon.messages.incoming.user.settings;

import net.h4bbo.lisbon.game.player.Player;
import net.h4bbo.lisbon.messages.outgoing.user.settings.ACCOUNT_PREFERENCES;
import net.h4bbo.lisbon.messages.types.MessageEvent;
import net.h4bbo.lisbon.server.netty.streams.NettyRequest;

public class GET_ACCOUNT_PREFERENCES implements MessageEvent {

    @Override
    public void handle(Player player, NettyRequest reader) {
        player.send(new ACCOUNT_PREFERENCES(player.getDetails()));
    }
}
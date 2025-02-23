package net.h4bbo.lisbon.messages.incoming.messenger;

import net.h4bbo.lisbon.game.player.Player;
import net.h4bbo.lisbon.messages.outgoing.messenger.FRIEND_REQUESTS;
import net.h4bbo.lisbon.messages.types.MessageEvent;
import net.h4bbo.lisbon.server.netty.streams.NettyRequest;

public class MESSENGER_GETREQUESTS implements MessageEvent {
    @Override
    public void handle(Player player, NettyRequest reader) throws Exception {
        player.send(new FRIEND_REQUESTS(player.getMessenger().getRequests()));
    }
}

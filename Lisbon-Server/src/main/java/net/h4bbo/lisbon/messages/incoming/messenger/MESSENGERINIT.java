package net.h4bbo.lisbon.messages.incoming.messenger;

import net.h4bbo.lisbon.game.messenger.Messenger;
import net.h4bbo.lisbon.game.messenger.MessengerManager;
import net.h4bbo.lisbon.game.player.Player;
import net.h4bbo.lisbon.messages.outgoing.messenger.MESSENGER_INIT;
import net.h4bbo.lisbon.messages.types.MessageEvent;
import net.h4bbo.lisbon.server.netty.streams.NettyRequest;

public class MESSENGERINIT implements MessageEvent {
    @Override
    public void handle(Player player, NettyRequest reader) throws Exception {
        Messenger messenger = MessengerManager.getInstance().getMessengerData(player.getDetails().getId());
        player.send(new MESSENGER_INIT(player, messenger));
    }
}

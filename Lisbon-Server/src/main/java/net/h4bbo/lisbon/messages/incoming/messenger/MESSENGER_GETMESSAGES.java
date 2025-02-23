package net.h4bbo.lisbon.messages.incoming.messenger;

import net.h4bbo.lisbon.game.messenger.MessengerMessage;
import net.h4bbo.lisbon.game.player.Player;
import net.h4bbo.lisbon.messages.outgoing.messenger.MESSENGER_MSG;
import net.h4bbo.lisbon.messages.types.MessageEvent;
import net.h4bbo.lisbon.server.netty.streams.NettyRequest;

public class MESSENGER_GETMESSAGES implements MessageEvent {
    @Override
    public void handle(Player player, NettyRequest reader) throws Exception {
        for (MessengerMessage offlineMessage : player.getMessenger().getOfflineMessages().values()) {
            player.send(new MESSENGER_MSG(offlineMessage));
        }
    }
}

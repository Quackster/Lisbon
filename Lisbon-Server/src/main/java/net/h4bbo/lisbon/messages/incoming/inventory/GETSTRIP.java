package net.h4bbo.lisbon.messages.incoming.inventory;

import net.h4bbo.lisbon.game.player.Player;
import net.h4bbo.lisbon.messages.types.MessageEvent;
import net.h4bbo.lisbon.server.netty.streams.NettyRequest;

public class GETSTRIP implements MessageEvent {
    @Override
    public void handle(Player player, NettyRequest reader) {
        String stripView = reader.contents();
        player.getInventory().getView(stripView);
    }
}

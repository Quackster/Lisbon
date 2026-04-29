package net.h4bbo.lisbon.messages.types;

import net.h4bbo.lisbon.game.player.Player;
import net.h4bbo.lisbon.server.netty.streams.NettyRequest;

public interface MessageEvent {
    
    /**
     * Handle the incoming client message.
     *
     * @param player the player
     * @param reader the reader
     */
    void handle(Player player, NettyRequest reader) throws Exception;
}

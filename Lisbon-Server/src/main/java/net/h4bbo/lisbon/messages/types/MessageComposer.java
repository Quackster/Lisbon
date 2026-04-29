package net.h4bbo.lisbon.messages.types;

import net.h4bbo.lisbon.server.netty.streams.NettyResponse;

public abstract class MessageComposer {
    /**
     * Write the message to send back to the client.
     */
    public abstract void compose(NettyResponse response);

    /**
     * Get the header
     */
    public abstract short getHeader();
}

package net.h4bbo.lisbon.messages.outgoing.handshake;

import net.h4bbo.lisbon.messages.types.MessageComposer;
import net.h4bbo.lisbon.server.netty.streams.NettyResponse;

public class HELLO extends MessageComposer {

    @Override
    public void compose(NettyResponse response) {

    }

    @Override
    public short getHeader() {
        return 0; // "@@"
    }
}

package net.h4bbo.lisbon.messages.outgoing.handshake;

import net.h4bbo.lisbon.messages.types.MessageComposer;
import net.h4bbo.lisbon.server.netty.streams.NettyResponse;

public class END_OF_CRYPTO_PARAMS extends MessageComposer {

    @Override
    public void compose(NettyResponse response) {
    }

    @Override
    public short getHeader() {
        return 278;
    }
}

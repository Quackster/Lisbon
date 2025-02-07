package net.h4bbo.lisbon.messages.outgoing.handshake;

import net.h4bbo.lisbon.messages.types.MessageComposer;
import net.h4bbo.lisbon.server.netty.streams.NettyResponse;

public class CRYPTO_PARAMETERS extends MessageComposer {

    @Override
    public void compose(NettyResponse response) {
        response.writeInt(0);
    }

    @Override
    public short getHeader() {
        return 277;
    }
}

package net.h4bbo.lisbon.messages.outgoing.user.currencies;

import net.h4bbo.lisbon.messages.types.MessageComposer;
import net.h4bbo.lisbon.server.netty.streams.NettyResponse;

public class NO_TICKETS extends MessageComposer {
    @Override
    public void compose(NettyResponse response) {

    }

    @Override
    public short getHeader() {
        return 73; // "AI"
    }
}

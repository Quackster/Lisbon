package net.h4bbo.lisbon.messages.outgoing.tutorial;

import net.h4bbo.lisbon.messages.types.MessageComposer;
import net.h4bbo.lisbon.server.netty.streams.NettyResponse;

public class INVITE_FOLLOW_FAILED extends MessageComposer {
    @Override
    public void compose(NettyResponse response) {

    }

    @Override
    public short getHeader() {
        return 359;
    }
}

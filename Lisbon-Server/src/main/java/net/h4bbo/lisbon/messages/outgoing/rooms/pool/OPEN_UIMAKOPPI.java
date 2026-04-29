package net.h4bbo.lisbon.messages.outgoing.rooms.pool;

import net.h4bbo.lisbon.messages.types.MessageComposer;
import net.h4bbo.lisbon.server.netty.streams.NettyResponse;

public class OPEN_UIMAKOPPI extends MessageComposer {

    @Override
    public void compose(NettyResponse response) {

    }

    @Override
    public short getHeader() {
        return 96; // "A`"
    }
}

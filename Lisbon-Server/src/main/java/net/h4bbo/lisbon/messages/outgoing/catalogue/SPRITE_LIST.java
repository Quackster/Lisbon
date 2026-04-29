package net.h4bbo.lisbon.messages.outgoing.catalogue;

import net.h4bbo.lisbon.messages.types.MessageComposer;
import net.h4bbo.lisbon.server.netty.streams.NettyResponse;

public class SPRITE_LIST extends MessageComposer {
    @Override
    public void compose(NettyResponse response) {
        response.writeInt(0);
    }

    @Override
    public short getHeader() {
        return 295; // "Dg"
    }
}

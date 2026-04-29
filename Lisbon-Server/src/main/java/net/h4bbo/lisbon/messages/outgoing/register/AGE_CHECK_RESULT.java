package net.h4bbo.lisbon.messages.outgoing.register;

import net.h4bbo.lisbon.messages.types.MessageComposer;
import net.h4bbo.lisbon.server.netty.streams.NettyResponse;

public class AGE_CHECK_RESULT extends MessageComposer {
    @Override
    public void compose(NettyResponse response) {
        response.writeBool(true);
    }

    @Override
    public short getHeader() {
        return 164;
    }
}

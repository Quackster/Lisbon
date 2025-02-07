package net.h4bbo.lisbon.messages.outgoing.user;

import net.h4bbo.lisbon.messages.types.MessageComposer;
import net.h4bbo.lisbon.server.netty.streams.NettyResponse;

public class IGNORE_USER_RESULT extends MessageComposer {
    private final int result;

    public IGNORE_USER_RESULT(int result) {
        this.result = result;
    }

    @Override
    public void compose(NettyResponse response) {
        response.writeInt(this.result);
    }

    @Override
    public short getHeader() {
        return 419;
    }
}

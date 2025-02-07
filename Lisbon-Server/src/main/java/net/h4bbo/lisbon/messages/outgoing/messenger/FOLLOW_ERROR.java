package net.h4bbo.lisbon.messages.outgoing.messenger;

import net.h4bbo.lisbon.messages.types.MessageComposer;
import net.h4bbo.lisbon.server.netty.streams.NettyResponse;

public class FOLLOW_ERROR extends MessageComposer {
    private final int errorId;

    public FOLLOW_ERROR(int errorId) {
        this.errorId = errorId;
    }

    @Override
    public void compose(NettyResponse response) {
        response.writeInt(this.errorId);
    }

    @Override
    public short getHeader() {
        return 349; // "E]"
    }
}

package net.h4bbo.lisbon.messages.outgoing.events;

import net.h4bbo.lisbon.messages.types.MessageComposer;
import net.h4bbo.lisbon.server.netty.streams.NettyResponse;

public class ROOMEVENT_TYPES extends MessageComposer {
    private final int count;

    public ROOMEVENT_TYPES(int count) {
        this.count = count;
    }

    @Override
    public void compose(NettyResponse response) {
        response.writeInt(this.count);
    }

    @Override
    public short getHeader() {
        return 368;
    }
}

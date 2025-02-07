package net.h4bbo.lisbon.messages.outgoing.welcomingparty;

import net.h4bbo.lisbon.messages.types.MessageComposer;
import net.h4bbo.lisbon.server.netty.streams.NettyResponse;

public class TUTORS_AVAILABLE extends MessageComposer {
    public final boolean available;

    public TUTORS_AVAILABLE(boolean available) {
        this.available = available;
    }

    @Override
    public void compose(NettyResponse response) {
        // Packet doesn't have any packet structure according to Lingo
        response.writeBool(this.available);
    }

    @Override
    public short getHeader() {
        return 356; // "Ed"
    }
}

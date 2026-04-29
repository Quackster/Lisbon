package net.h4bbo.lisbon.messages.outgoing.register;

import net.h4bbo.lisbon.messages.types.MessageComposer;
import net.h4bbo.lisbon.server.netty.streams.NettyResponse;

public class DATE extends MessageComposer {
    private final String shortDate;

    public DATE(String shortDate) {
        this.shortDate = shortDate;
    }

    @Override
    public void compose(NettyResponse response) {
        response.write(this.shortDate);
    }

    @Override
    public short getHeader() {
        return 163; // "Bc"
    }
}

package net.h4bbo.lisbon.messages.outgoing.rooms.infobus;

import net.h4bbo.lisbon.messages.types.MessageComposer;
import net.h4bbo.lisbon.server.netty.streams.NettyResponse;

public class CANNOT_ENTER_BUS extends MessageComposer {
    private final String message;

    public CANNOT_ENTER_BUS(String message) {
        this.message = message;
    }

    @Override
    public void compose(NettyResponse response) {
        response.writeString(this.message);
    }

    @Override
    public short getHeader() {
        return 81; // "AQ"
    }
}


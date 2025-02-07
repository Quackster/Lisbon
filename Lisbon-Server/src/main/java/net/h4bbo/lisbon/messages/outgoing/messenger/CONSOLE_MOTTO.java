package net.h4bbo.lisbon.messages.outgoing.messenger;

import net.h4bbo.lisbon.messages.types.MessageComposer;
import net.h4bbo.lisbon.server.netty.streams.NettyResponse;

public class CONSOLE_MOTTO extends MessageComposer {
    private final String consoleMotto;

    public CONSOLE_MOTTO(String consoleMotto) {
        this.consoleMotto = consoleMotto;
    }

    @Override
    public void compose(NettyResponse response) {
        response.writeString(this.consoleMotto);
    }

    @Override
    public short getHeader() {
        return 147; // "BS"
    }
}

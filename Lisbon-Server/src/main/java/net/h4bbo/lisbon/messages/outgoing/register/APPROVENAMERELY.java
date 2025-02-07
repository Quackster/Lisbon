package net.h4bbo.lisbon.messages.outgoing.register;

import net.h4bbo.lisbon.messages.types.MessageComposer;
import net.h4bbo.lisbon.server.netty.streams.NettyResponse;

public class APPROVENAMERELY extends MessageComposer {
    private final int nameCheckCode;

    public APPROVENAMERELY(int nameCheckCode) {
        this.nameCheckCode = nameCheckCode;
    }

    @Override
    public void compose(NettyResponse response) {
        response.writeInt(this.nameCheckCode);
    }

    @Override
    public short getHeader() {
        return 36;
    }
}

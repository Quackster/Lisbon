package net.h4bbo.lisbon.messages.outgoing.messenger;

import net.h4bbo.lisbon.messages.types.MessageComposer;
import net.h4bbo.lisbon.server.netty.streams.NettyResponse;

public class ROOMFORWARD extends MessageComposer {
    private final boolean isPublic;
    private int roomId;

    public ROOMFORWARD(boolean isPublic, int roomId) {
        this.isPublic = isPublic;
        this.roomId = roomId;
    }


    @Override
    public void compose(NettyResponse response) {
        response.writeBool(this.isPublic);
        response.writeInt(this.roomId);
    }

    @Override
    public short getHeader() {
        return 286; // "D^"
    }
}

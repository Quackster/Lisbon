package net.h4bbo.lisbon.messages.outgoing.rooms.pool;

import net.h4bbo.lisbon.messages.types.MessageComposer;
import net.h4bbo.lisbon.server.netty.streams.NettyResponse;

public class JUMPDATA extends MessageComposer {
    private final int instanceId;
    private final String divingHandle;

    public JUMPDATA(int instanceId, String divingHandle) {
        this.instanceId = instanceId;
        this.divingHandle = divingHandle;
    }

    @Override
    public void compose(NettyResponse response) {
        response.writeDelimeter(this.instanceId, (char)13);
        response.write(this.divingHandle);
    }

    @Override
    public short getHeader() {
        return 74; // "AJ"
    }
}

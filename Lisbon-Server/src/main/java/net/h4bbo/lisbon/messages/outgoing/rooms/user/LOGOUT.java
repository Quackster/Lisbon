package net.h4bbo.lisbon.messages.outgoing.rooms.user;

import net.h4bbo.lisbon.messages.types.MessageComposer;
import net.h4bbo.lisbon.server.netty.streams.NettyResponse;

public class LOGOUT extends MessageComposer {
    private final int instanceId;

    public LOGOUT(int instanceId) {
        this.instanceId = instanceId;
    }

    @Override
    public void compose(NettyResponse response) {
        response.write(this.instanceId);
    }

    @Override
    public short getHeader() {
        return 29; // "@]
    }
}

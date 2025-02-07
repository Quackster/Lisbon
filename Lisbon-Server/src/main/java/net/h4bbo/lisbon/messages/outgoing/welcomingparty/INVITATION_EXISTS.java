package net.h4bbo.lisbon.messages.outgoing.welcomingparty;

import net.h4bbo.lisbon.messages.types.MessageComposer;
import net.h4bbo.lisbon.server.netty.streams.NettyResponse;

public class INVITATION_EXISTS extends MessageComposer {
    public INVITATION_EXISTS() {

    }

    @Override
    public void compose(NettyResponse response) {
        // Packet doesn't have any packet structure according to Lingo
    }

    @Override
    public short getHeader() {
        return 358; // "Ef"
    }
}

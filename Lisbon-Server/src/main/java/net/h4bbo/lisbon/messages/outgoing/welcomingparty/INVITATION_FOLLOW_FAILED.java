package net.h4bbo.lisbon.messages.outgoing.welcomingparty;

import net.h4bbo.lisbon.messages.types.MessageComposer;
import net.h4bbo.lisbon.server.netty.streams.NettyResponse;

public class INVITATION_FOLLOW_FAILED extends MessageComposer {
    public INVITATION_FOLLOW_FAILED() {

    }

    @Override
    public void compose(NettyResponse response) {
        // Packet doesn't have any packet structure according to Lingo
    }

    @Override
    public short getHeader() {
        return 359; // "Eg"
    }
}

package net.h4bbo.lisbon.messages.outgoing.games;

import net.h4bbo.lisbon.messages.types.MessageComposer;
import net.h4bbo.lisbon.server.netty.streams.NettyResponse;

public class LOUNGEINFO extends MessageComposer {
    @Override
    public void compose(NettyResponse response) {
        response.writeInt(0);
        //response.writeString("Rank name"); // Rank name here
        //response.writeInt(1); // Minimum points
        //response.writeInt(1); // Maximum points
    }

    @Override
    public short getHeader() {
        return 231; // "Cg"
    }
}

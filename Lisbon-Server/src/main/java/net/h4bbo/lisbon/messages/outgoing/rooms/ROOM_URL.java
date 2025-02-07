package net.h4bbo.lisbon.messages.outgoing.rooms;

import net.h4bbo.lisbon.messages.types.MessageComposer;
import net.h4bbo.lisbon.server.netty.streams.NettyResponse;

public class ROOM_URL extends MessageComposer {

    @Override
    public void compose(NettyResponse response) {
        response.writeString("/client/");
    }

    @Override
    public short getHeader() {
        return 166; // "Bf"
    }
}

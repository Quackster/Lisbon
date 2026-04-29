package net.h4bbo.lisbon.messages.outgoing.rooms.settings;

import net.h4bbo.lisbon.messages.types.MessageComposer;
import net.h4bbo.lisbon.server.netty.streams.NettyResponse;

public class GOTO_FLAT extends MessageComposer {
    private final int roomId;
    private final String roomName;

    public GOTO_FLAT(int roomId, String roomName) {
        this.roomId = roomId;
        this.roomName = roomName;
    }

    @Override
    public void compose(NettyResponse response) {
        response.writeDelimeter(this.roomId, (char)13);
        response.write(this.roomName);
    }

    @Override
    public short getHeader() {
        return 59; // "@{"
    }
}

package net.h4bbo.lisbon.messages.outgoing.games;

import net.h4bbo.lisbon.messages.types.MessageComposer;
import net.h4bbo.lisbon.server.netty.streams.NettyResponse;

public class GAMESTART extends MessageComposer {
    private final int gameLengthSeconds;

    public GAMESTART(int gameLengthSeconds) {
        this.gameLengthSeconds = gameLengthSeconds;
    }

    @Override
    public void compose(NettyResponse response) {
        response.writeInt(this.gameLengthSeconds);
    }

    @Override
    public short getHeader() {
        return 247; // "Cw"
    }
}

package net.h4bbo.lisbon.messages.outgoing.handshake;

import net.h4bbo.lisbon.game.fuserights.Fuseright;
import net.h4bbo.lisbon.messages.types.MessageComposer;
import net.h4bbo.lisbon.server.netty.streams.NettyResponse;

import java.util.List;

public class RIGHTS extends MessageComposer {
    private final List<Fuseright> avaliableFuserights;

    public RIGHTS(List<Fuseright> avaliableFuserights) {
        this.avaliableFuserights = avaliableFuserights;
    }

    @Override
    public void compose(NettyResponse response) {
        for (Fuseright fuseright : this.avaliableFuserights) {
            response.writeString(fuseright.getFuseright());
        }
    }

    @Override
    public short getHeader() {
        return 2; // "@B"
    }
}

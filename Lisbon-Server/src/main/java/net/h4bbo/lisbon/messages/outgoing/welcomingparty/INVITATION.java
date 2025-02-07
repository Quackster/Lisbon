package net.h4bbo.lisbon.messages.outgoing.welcomingparty;

import net.h4bbo.lisbon.game.player.PlayerDetails;
import net.h4bbo.lisbon.messages.types.MessageComposer;
import net.h4bbo.lisbon.server.netty.streams.NettyResponse;

public class INVITATION extends MessageComposer {
    private final PlayerDetails inviter;

    public INVITATION(PlayerDetails inviter) {
        this.inviter = inviter;
    }

    @Override
    public void compose(NettyResponse response) {
        response.writeString(this.inviter.getId());
        response.writeString(this.inviter.getName());
    }

    @Override
    public short getHeader() {
        return 355; // "Ec"
    }
}

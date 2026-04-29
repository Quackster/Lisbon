package net.h4bbo.lisbon.messages.outgoing.user.currencies;

import net.h4bbo.lisbon.game.player.PlayerDetails;
import net.h4bbo.lisbon.messages.types.MessageComposer;
import net.h4bbo.lisbon.server.netty.streams.NettyResponse;

public class CREDIT_BALANCE extends MessageComposer {
    private final int credits;

    public CREDIT_BALANCE(int credits) {
        this.credits = credits;
    }

    @Override
    public void compose(NettyResponse response) {
        response.writeString(this.credits + ".0");
    }

    @Override
    public short getHeader() {
        return 6; // "@F
    }
}

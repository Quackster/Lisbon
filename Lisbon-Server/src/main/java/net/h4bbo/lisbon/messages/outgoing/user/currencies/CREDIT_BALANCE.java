package net.h4bbo.lisbon.messages.outgoing.user.currencies;

import net.h4bbo.lisbon.game.player.PlayerDetails;
import net.h4bbo.lisbon.messages.types.MessageComposer;
import net.h4bbo.lisbon.server.netty.streams.NettyResponse;

public class CREDIT_BALANCE extends MessageComposer {
    private final PlayerDetails details;

    public CREDIT_BALANCE(PlayerDetails details) {
        this.details = details;
    }


    @Override
    public void compose(NettyResponse response) {
        response.writeString(this.details.getCredits() + ".0");
    }

    @Override
    public short getHeader() {
        return 6; // "@F
    }
}

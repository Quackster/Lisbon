package net.h4bbo.lisbon.messages.outgoing.user.currencies;

import net.h4bbo.lisbon.messages.types.MessageComposer;
import net.h4bbo.lisbon.server.netty.streams.NettyResponse;

public class TICKET_BALANCE extends MessageComposer {
    private final int tickets;

    public TICKET_BALANCE(int tickets) {
        this.tickets = tickets;
    }

    @Override
    public void compose(NettyResponse response) {
        response.write(this.tickets);
    }

    @Override
    public short getHeader() {
        return 124; // "A|"
    }
}

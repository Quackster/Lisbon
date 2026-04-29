package net.h4bbo.lisbon.messages.outgoing.rooms.items;

import net.h4bbo.lisbon.messages.types.MessageComposer;
import net.h4bbo.lisbon.server.netty.streams.NettyResponse;

public class DICE_VALUE extends MessageComposer {
    private final int itemId;
    private final boolean spin;
    private final int randomNumber;

    public DICE_VALUE(int itemId, boolean spin, int randomNumber) {
        this.itemId = itemId;
        this.spin = spin;
        this.randomNumber = randomNumber;
    }

    @Override
    public void compose(NettyResponse response) {
        response.write(this.itemId);

        if (!this.spin) {
            if (this.randomNumber > 0) {
                response.write(" " + ((this.itemId * 38) + this.randomNumber));
            } else {
                response.write(" " + (this.itemId * 38));
            }
        }
    }

    @Override
    public short getHeader() {
        return 90; // "AZ"
    }
}


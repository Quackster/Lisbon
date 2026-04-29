package net.h4bbo.lisbon.messages.outgoing.rooms.items;

import net.h4bbo.lisbon.game.item.Item;
import net.h4bbo.lisbon.messages.types.MessageComposer;
import net.h4bbo.lisbon.server.netty.streams.NettyResponse;

public class PLACE_WALLITEM extends MessageComposer {
    private final Item item;

    public PLACE_WALLITEM(Item item) {
        this.item = item;
    }

    @Override
    public void compose(NettyResponse response) {
        this.item.serialise(response);
    }

    @Override
    public short getHeader() {
        return 83; // "AS"
    }
}

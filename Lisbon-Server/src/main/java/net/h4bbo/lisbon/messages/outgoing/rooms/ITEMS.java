package net.h4bbo.lisbon.messages.outgoing.rooms;

import net.h4bbo.lisbon.game.item.Item;
import net.h4bbo.lisbon.game.room.Room;
import net.h4bbo.lisbon.messages.types.MessageComposer;
import net.h4bbo.lisbon.server.netty.streams.NettyResponse;

import java.util.List;

public class ITEMS extends MessageComposer {
    private final List<Item> items;

    public ITEMS(Room room) {
        this.items = room.getItemManager().getWallItems();
    }

    @Override
    public void compose(NettyResponse response) {
        //response.writeInt(this.items.size());

        for (Item item : this.items) {
            item.serialise(response);
        }
    }
    @Override
    public short getHeader() {
        return 45; // "@m"
    }
}

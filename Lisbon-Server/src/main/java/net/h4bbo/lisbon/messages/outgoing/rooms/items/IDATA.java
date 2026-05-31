package net.h4bbo.lisbon.messages.outgoing.rooms.items;

import net.h4bbo.lisbon.game.item.Item;
import net.h4bbo.lisbon.game.item.base.ItemBehaviour;
import net.h4bbo.lisbon.messages.types.MessageComposer;
import net.h4bbo.lisbon.server.netty.streams.NettyResponse;

public class IDATA extends MessageComposer {
    private String colour;
    private String text;
    private Item item;

    public IDATA(Item item, String colour, String text) {
        this.item = item;
        this.colour = colour;
        this.text = text;
    }

    public IDATA(Item item) {
        this.item = item;
    }

    @Override
    public void compose(NettyResponse response) {
        boolean isPhoto = "photo".equalsIgnoreCase(this.item.getDefinition().getSprite());

        if (this.item.hasBehaviour(ItemBehaviour.POST_IT)) {
            response.writeDelimeter(this.item.getId(), (char) 9);
            response.writeDelimeter(this.colour, ' ');
            response.write(this.text);
        } else {
            response.writeDelimeter(this.item.getId(), (char) 9);
            if (isPhoto) {
                response.writeDelimeter("I", ' ');
            } else {
                response.writeDelimeter(this.item.hasBehaviour(ItemBehaviour.WALL_ITEM) ? "I" : "S", ' ');
                response.writeDelimeter(this.item.getOwnerId(), ' ');
            }
            response.write(this.item.getCustomData());
        }
    }

    @Override
    public short getHeader() {
        return 48; // "@p"
    }
}

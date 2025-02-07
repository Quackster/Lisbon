package net.h4bbo.lisbon.messages.outgoing.jukebox;

import net.h4bbo.lisbon.game.item.Item;
import net.h4bbo.lisbon.messages.types.MessageComposer;
import net.h4bbo.lisbon.server.netty.streams.NettyResponse;

import java.util.Map;

public class USER_SONG_DISKS extends MessageComposer {
    private final Map<Item, Integer> userDisks;

    public USER_SONG_DISKS(Map<Item, Integer> userDisks) {
        this.userDisks = userDisks;
    }

    @Override
    public void compose(NettyResponse response) {
        response.writeInt(this.userDisks.size());

        for (var kvp : this.userDisks.entrySet()) {
            response.writeInt(kvp.getValue());
            response.writeString(kvp.getKey().getCustomData().split(Character.toString((char)10))[5]);
        }
    }

    @Override
    public short getHeader() {
        return 333;
    }
}

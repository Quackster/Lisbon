package net.h4bbo.lisbon.messages.outgoing.rooms;

import net.h4bbo.lisbon.game.room.models.RoomModel;
import net.h4bbo.lisbon.messages.types.MessageComposer;
import net.h4bbo.lisbon.server.netty.streams.NettyResponse;

public class HEIGHTMAP extends MessageComposer {
    private final String heightmap;

    public HEIGHTMAP(String heightmap) {
        this.heightmap = heightmap;
    }

    public HEIGHTMAP(RoomModel roomModel) {
        this.heightmap = roomModel.getHeightmap();
    }

    @Override
    public void compose(NettyResponse response) {
        response.write(this.heightmap);
    }

    @Override
    public short getHeader() {
        return 31; // "@_"
    }
}

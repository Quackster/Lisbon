package net.h4bbo.lisbon.messages.outgoing.rooms.groups;

import net.h4bbo.lisbon.game.groups.Group;
import net.h4bbo.lisbon.game.room.RoomManager;
import net.h4bbo.lisbon.messages.types.MessageComposer;
import net.h4bbo.lisbon.server.netty.streams.NettyResponse;

public class GROUP_INFO extends MessageComposer {
    private final Group group;

    public GROUP_INFO(Group group) {
        this.group = group;
    }

    @Override
    public void compose(NettyResponse response) {
        response.writeInt(this.group.getId());
        response.writeString(this.group.getName());
        response.writeString(this.group.getDescription());

        var room = RoomManager.getInstance().getRoomById(this.group.getRoomId());

        if (this.group.getRoomId() > 0 && room != null) {
            response.writeInt(this.group.getRoomId());
            response.writeString(room.getData().getName());
        } else {
            response.writeInt(-1);
            response.writeString("");
        }
    }

    @Override
    public short getHeader() {
        return 311;
    }
}

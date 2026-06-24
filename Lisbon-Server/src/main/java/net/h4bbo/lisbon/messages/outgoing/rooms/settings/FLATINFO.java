package net.h4bbo.lisbon.messages.outgoing.rooms.settings;

import net.h4bbo.lisbon.game.fuserights.Fuseright;
import net.h4bbo.lisbon.game.player.Player;
import net.h4bbo.lisbon.game.room.Room;
import net.h4bbo.lisbon.messages.types.MessageComposer;
import net.h4bbo.lisbon.server.netty.streams.NettyResponse;

public class FLATINFO extends MessageComposer {
    private Player player;
    private Room room;

    public FLATINFO(Player player, Room room) {
        this.player = player;
        this.room = room;
    }

    @Override
    public void compose(NettyResponse response) {
        response.writeBool(this.room.getData().allowSuperUsers());
        response.writeInt(this.room.getData().getAccessTypeId());
        response.writeInt(this.room.getId());

        if (this.room.isOwner(player.getDetails().getId())|| this.room.getData().showOwnerName() || this.player.hasFuse(Fuseright.SEE_ALL_ROOMOWNERS)) {
            response.writeString(this.room.getData().getOwnerName());
        } else {
            response.writeString("-");
        }

        response.writeString(this.room.getModel().getName()); // Is called "marker" in Lingo code
        response.writeString(this.room.getData().getName());
        response.writeString(this.room.getData().getDescription());
        response.writeBool(this.room.getData().showOwnerName());
        response.writeBool(this.room.getCategory().hasAllowTrading()); // Allow trading
        response.writeBool(this.room.getCategory() == null);
        response.writeInt(this.room.getData().getVisitorsMax()); // Our current max visitors setting
        response.writeInt(100); // The actual allowed max visitors, always 100 or less.
    }

    @Override
    public short getHeader() {
        return 54; // "@v"
    }
}


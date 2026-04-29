package net.h4bbo.lisbon.messages.outgoing.user;

import net.h4bbo.lisbon.game.player.PlayerDetails;
import net.h4bbo.lisbon.messages.types.MessageComposer;
import net.h4bbo.lisbon.server.netty.streams.NettyResponse;

public class USER_OBJECT extends MessageComposer {
    private final PlayerDetails details;

    public USER_OBJECT(PlayerDetails details) {
        this.details = details;
    }

    @Override
    public void compose(NettyResponse response) {
        response.writeString(this.details.getId());
        response.writeString(this.details.getName());
        response.writeString(this.details.getFigure());
        response.writeString(this.details.getSex());
        response.writeString(this.details.getMotto());
        response.writeInt(this.details.getTickets());
        response.writeString(this.details.getPoolFigure());
        response.writeInt(this.details.getFilm());
        response.writeBool(false); // directMail
    }

    @Override
    public short getHeader() {
        return 5; // "@E"
    }
}

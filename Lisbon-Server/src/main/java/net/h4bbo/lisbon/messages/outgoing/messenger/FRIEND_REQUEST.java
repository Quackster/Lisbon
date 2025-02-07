package net.h4bbo.lisbon.messages.outgoing.messenger;

import net.h4bbo.lisbon.game.messenger.MessengerUser;
import net.h4bbo.lisbon.messages.types.MessageComposer;
import net.h4bbo.lisbon.server.netty.streams.NettyResponse;

public class FRIEND_REQUEST extends MessageComposer {
    private final MessengerUser requester;

    public FRIEND_REQUEST(MessengerUser requester) {
        this.requester = requester;
    }

    @Override
    public void compose(NettyResponse response) {
        response.writeInt(this.requester.getUserId());
        response.writeString(this.requester.getUsername());
        response.writeString(String.valueOf(this.requester.getUserId()));
    }

    @Override
    public short getHeader() {
        return 132;
    }
}

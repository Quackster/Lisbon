package net.h4bbo.lisbon.messages.outgoing.messenger;

import net.h4bbo.lisbon.game.messenger.MessengerUser;
import net.h4bbo.lisbon.messages.types.MessageComposer;
import net.h4bbo.lisbon.server.netty.streams.NettyResponse;

import java.util.List;

public class FRIEND_REQUESTS extends MessageComposer {
    private final List<MessengerUser> requests;

    public FRIEND_REQUESTS(List<MessengerUser> requests) {
        this.requests = requests;
    }

    @Override
    public void compose(NettyResponse response) {
        response.writeInt(this.requests.size());
        response.writeInt(this.requests.size());

        for (MessengerUser messengerUser : this.requests) {
            response.writeInt(messengerUser.getUserId());
            response.writeString(messengerUser.getUsername());
            response.writeString(messengerUser.getUserId());
        }
    }

    @Override
    public short getHeader() {
        return 314; // "BD"
    }
}

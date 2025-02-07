package net.h4bbo.lisbon.messages.outgoing.messenger;

import net.h4bbo.lisbon.game.messenger.MessengerMessage;
import net.h4bbo.lisbon.messages.types.MessageComposer;
import net.h4bbo.lisbon.server.netty.streams.NettyResponse;

public class MESSENGER_MSG extends MessageComposer {
    private final MessengerMessage message;

    public MESSENGER_MSG(MessengerMessage message) {
        this.message = message;
    }

    @Override
    public void compose(NettyResponse response) {
        //response.writeInt(this.message.getVirtualId());
        response.writeInt(this.message.getFromId());
        //response.writeString(DateUtil.getDateAsString(this.message.getTimeSet()));
        response.writeString(this.message.getMessage());
    }

    @Override
    public short getHeader() {
        return 134; // "BF"
    }
}

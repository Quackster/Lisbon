package net.h4bbo.lisbon.messages.outgoing.moderation;

import net.h4bbo.lisbon.game.moderation.cfh.CallForHelp;
import net.h4bbo.lisbon.messages.types.MessageComposer;
import net.h4bbo.lisbon.server.netty.streams.NettyResponse;

public class PICKED_CRY extends MessageComposer {
    private CallForHelp cfh;

    public PICKED_CRY(CallForHelp cfh){
        this.cfh = cfh;
    }

    @Override
    public void compose(NettyResponse response) {
        response.writeString(cfh.getCryId());
        response.writeString(cfh.getPickedUpBy());
    }

    @Override
    public short getHeader() {
        return 149; // "BU"
    }
}

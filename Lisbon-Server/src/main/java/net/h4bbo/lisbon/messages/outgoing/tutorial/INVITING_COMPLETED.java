package net.h4bbo.lisbon.messages.outgoing.tutorial;

import net.h4bbo.lisbon.messages.types.MessageComposer;
import net.h4bbo.lisbon.server.netty.streams.NettyResponse;

public class INVITING_COMPLETED extends MessageComposer {
    public enum InvitationResult {
        SUCCESS,
        FAILURE;
    }

    private InvitationResult result;

    public INVITING_COMPLETED(InvitationResult result) {
        this.result = result;
    }

    @Override
    public void compose(NettyResponse response) {
        if (this.result == InvitationResult.SUCCESS) {
            response.writeBool(true);
        } else {
            response.writeBool(false);
        }
    }

    @Override
    public short getHeader() {
        return 357; // "Ee"
    }
}


package net.h4bbo.lisbon.messages.outgoing.rooms.badges;

import net.h4bbo.lisbon.game.player.PlayerDetails;
import net.h4bbo.lisbon.messages.types.MessageComposer;
import net.h4bbo.lisbon.server.netty.streams.NettyResponse;

public class USER_BADGE extends MessageComposer {
    private int instanceId;
    private String currentBadge;
    private boolean showBadge;

    public USER_BADGE(int instanceId, PlayerDetails playerDetails) {
        this.instanceId = instanceId;
        this.currentBadge = null; //playerDetails.getCurrentBadge();
        this.showBadge = false;//playerDetails.getShowBadge();
    }

    @Override
    public void compose(NettyResponse response) {
        response.writeInt(this.instanceId);

        if (this.showBadge) {
            response.writeString(this.currentBadge);
        }
    }

    @Override
    public short getHeader() {
        return 228; // "Cd"
    }
}

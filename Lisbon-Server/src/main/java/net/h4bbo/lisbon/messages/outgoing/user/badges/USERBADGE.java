package net.h4bbo.lisbon.messages.outgoing.user.badges;

import net.h4bbo.lisbon.game.badges.Badge;
import net.h4bbo.lisbon.messages.types.MessageComposer;
import net.h4bbo.lisbon.server.netty.streams.NettyResponse;

import java.util.List;

public class USERBADGE extends MessageComposer {
    private final int userId;
    private final List<Badge> equippedBadges;

    public USERBADGE(int userId, List<Badge> equippedBadges) {
        this.userId = userId;
        this.equippedBadges = equippedBadges;
    }

    @Override
    public void compose(NettyResponse response) {
        response.writeString(this.userId);
        response.writeInt(this.equippedBadges.size());

        for (Badge badge : this.equippedBadges) {
            response.writeInt(badge.getSlotId());
            response.writeString(badge.getBadgeCode());
        }
    }

    @Override
    public short getHeader() {
        return 228; // "Cd"
    }
}

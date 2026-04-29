package net.h4bbo.lisbon.messages.outgoing.rooms.badges;

import net.h4bbo.lisbon.game.badges.Badge;
import net.h4bbo.lisbon.messages.types.MessageComposer;
import net.h4bbo.lisbon.server.netty.streams.NettyResponse;

import java.util.List;

public class AVAILABLE_BADGES extends MessageComposer {
    private final List<Badge> badges;
    private final List<Badge> equippedBadges;

    public AVAILABLE_BADGES(List<Badge> badges, List<Badge> equippedBadges) {
        this.badges = badges;
        this.equippedBadges = equippedBadges;
    }


    @Override
    public void compose(NettyResponse response) {
        response.writeInt(this.badges.size());

        for (Badge badge : this.badges) {
            response.writeString(badge.getBadgeCode());
        }

        response.writeInt(this.equippedBadges.size());

        for (Badge badge : this.equippedBadges) {
            response.writeInt(badge.getSlotId());
            response.writeString(badge.getBadgeCode());
        }
    }

    @Override
    public short getHeader() {
        return 229; // "Ce"
    }
}

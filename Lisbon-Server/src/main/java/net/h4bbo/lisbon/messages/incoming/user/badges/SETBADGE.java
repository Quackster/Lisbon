package net.h4bbo.lisbon.messages.incoming.user.badges;


import net.h4bbo.lisbon.game.badges.Badge;
import net.h4bbo.lisbon.game.player.Player;
import net.h4bbo.lisbon.messages.outgoing.user.badges.USERBADGE;
import net.h4bbo.lisbon.messages.types.MessageEvent;
import net.h4bbo.lisbon.server.netty.streams.NettyRequest;

public class SETBADGE implements MessageEvent {
    @Override
    public void handle(Player player, NettyRequest reader) throws Exception {
        // Unequip all previous badges
        for (Badge badge : player.getBadgeManager().getBadges()) {
            player.getBadgeManager().changeBadge(badge.getBadgeCode(), false, 0);
        }

        // Equip new badges
        while (reader.contents().length() > 0) {
            int slotId = reader.readInt();
            String badgeCode = reader.readString();

            if (slotId > 0 && slotId < 6 && badgeCode.length() > 0) {
                player.getBadgeManager().changeBadge(badgeCode, true, slotId);
            }
        }

        // Notify users of badge updates
        if (player.getRoomUser().getRoom() != null) {
            player.getRoomUser().getRoom().send(new USERBADGE(player.getDetails().getId(), player.getBadgeManager().getEquippedBadges()));
        }
        
        player.getBadgeManager().refreshBadges();
        player.getBadgeManager().saveQueuedBadges();
    }
}

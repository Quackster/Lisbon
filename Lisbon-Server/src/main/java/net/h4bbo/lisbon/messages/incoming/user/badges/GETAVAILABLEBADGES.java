package net.h4bbo.lisbon.messages.incoming.user.badges;

import net.h4bbo.lisbon.game.player.Player;
import net.h4bbo.lisbon.messages.types.MessageEvent;
import net.h4bbo.lisbon.server.netty.streams.NettyRequest;

public class GETAVAILABLEBADGES implements MessageEvent {
    @Override
    public void handle(Player player, NettyRequest reader) throws Exception {
        if (!player.isLoggedIn()) {
            return;
        }

        player.getBadgeManager().refreshBadges();
        player.getAchievementManager().processAchievements(player, true);
    }
}

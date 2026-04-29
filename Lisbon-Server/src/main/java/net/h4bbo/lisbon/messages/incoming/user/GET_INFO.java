package net.h4bbo.lisbon.messages.incoming.user;

import net.h4bbo.lisbon.game.player.Player;
import net.h4bbo.lisbon.messages.outgoing.user.USER_OBJECT;
import net.h4bbo.lisbon.messages.types.MessageEvent;
import net.h4bbo.lisbon.server.netty.streams.NettyRequest;

public class GET_INFO implements MessageEvent {

    @Override
    public void handle(Player player, NettyRequest reader) {
        if (!player.isLoggedIn()) {
            return;
        }

        player.getBadgeManager().refreshBadges();
        player.getAchievementManager().processAchievements(player, true);

        player.send(new USER_OBJECT(player.getDetails()));
    }
}

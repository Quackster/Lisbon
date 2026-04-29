package net.h4bbo.lisbon.messages.incoming.club;

import net.h4bbo.lisbon.game.club.ClubSubscription;
import net.h4bbo.lisbon.game.player.Player;
import net.h4bbo.lisbon.log.Log;
import net.h4bbo.lisbon.messages.types.MessageEvent;
import net.h4bbo.lisbon.server.netty.streams.NettyRequest;

import java.sql.SQLException;

public class SCR_GIFT_APPROVAL implements MessageEvent {
    @Override
    public void handle(Player player, NettyRequest reader) throws Exception {
        if (ClubSubscription.isGiftDue(player)) {

            try {
                ClubSubscription.tryNextGift(player);
            } catch (SQLException e) {
                Log.getErrorLogger().error("Error trying to process club gift for user (" + player.getDetails().getName() + "): ", e);
            }
        }
    }
}

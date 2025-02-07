package net.h4bbo.lisbon.messages.incoming.club;

import net.h4bbo.lisbon.game.club.ClubSubscription;
import net.h4bbo.lisbon.game.player.Player;
import net.h4bbo.lisbon.messages.outgoing.club.CLUB_GIFT;
import net.h4bbo.lisbon.messages.types.MessageEvent;
import net.h4bbo.lisbon.server.netty.streams.NettyRequest;

public class SUBSCRIBE_CLUB implements MessageEvent {
    @Override
    public void handle(Player player, NettyRequest reader) throws Exception {
        reader.readString();

        int days = -1;
        int credits = -1;

        int choice = reader.readInt();

        switch (choice) {
            case 1:
            {
                credits = 25;
                days = 31;
                break;
            }
            case 2:
            {
                credits = 60;
                days = 93;
                break;
            }
            case 3:
            {
                credits = 105;
                days = 186;
                break;
            }
        }

        ClubSubscription.subscribeClub(player, days, credits);

        if (ClubSubscription.isGiftDue(player)) {
            player.send(new CLUB_GIFT(1));
        }
    }
}

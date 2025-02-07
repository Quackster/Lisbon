package net.h4bbo.lisbon.messages.incoming.rooms.badges;

import net.h4bbo.lisbon.dao.mysql.BadgeDao;
import net.h4bbo.lisbon.game.player.Player;
import net.h4bbo.lisbon.messages.outgoing.rooms.badges.USER_BADGE;
import net.h4bbo.lisbon.messages.types.MessageEvent;
import net.h4bbo.lisbon.server.netty.streams.NettyRequest;

public class SETBADGE implements MessageEvent {
    @Override
    public void handle(Player player, NettyRequest reader) {
        reader.readInt();

        String newBadge = reader.readString();

        if (!player.getDetails().getBadges().contains(newBadge)) {
            return;
        }

        boolean showBadge = reader.readBoolean();

        player.getDetails().setCurrentBadge(newBadge);
        player.getDetails().setShowBadge(showBadge);

        if (player.getRoomUser().getRoom() != null) {
            player.getRoomUser().getRoom().send(new USER_BADGE(player.getRoomUser().getInstanceId(), player.getDetails()));
        }

        BadgeDao.saveCurrentBadge(player.getDetails());
    }
}
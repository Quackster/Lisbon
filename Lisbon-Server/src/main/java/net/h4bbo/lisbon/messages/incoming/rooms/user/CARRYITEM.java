package net.h4bbo.lisbon.messages.incoming.rooms.user;

import net.h4bbo.lisbon.game.player.Player;
import net.h4bbo.lisbon.messages.outgoing.user.currencies.FILM;
import net.h4bbo.lisbon.messages.types.MessageEvent;
import net.h4bbo.lisbon.server.netty.streams.NettyRequest;
import org.apache.commons.lang3.StringUtils;

public class CARRYITEM implements MessageEvent {
    @Override
    public void handle(Player player, NettyRequest reader) throws Exception {
        if (player.getRoomUser().getRoom() == null) {
            return;
        }

        String contents = reader.contents();

        if (contents.equals("20")) {
            player.send(new FILM(player.getDetails()));

            if (StringUtils.isNumeric(contents)) {
                player.getRoomUser().carryItem(Integer.parseInt(contents), null);
            } else {
                player.getRoomUser().carryItem(-1, contents);
            }

            player.getRoomUser().getTimerManager().resetRoomTimer();
        }
    }
}

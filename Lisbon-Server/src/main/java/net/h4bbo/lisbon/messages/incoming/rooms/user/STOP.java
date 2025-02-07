package net.h4bbo.lisbon.messages.incoming.rooms.user;

import net.h4bbo.lisbon.game.player.Player;
import net.h4bbo.lisbon.game.room.enums.StatusType;
import net.h4bbo.lisbon.messages.types.MessageEvent;
import net.h4bbo.lisbon.server.netty.streams.NettyRequest;

public class STOP implements MessageEvent {
    @Override
    public void handle(Player player, NettyRequest reader) {
        String stopWhat = reader.contents();

        if (stopWhat.equals("Dance")) {
            player.getRoomUser().removeStatus(StatusType.DANCE);
            player.getRoomUser().setNeedsUpdate(true);
        }

        if (stopWhat.equals("CarryItem")) {
            player.getRoomUser().removeStatus(StatusType.CARRY_ITEM);
            player.getRoomUser().setNeedsUpdate(true);
        }

        player.getRoomUser().getTimerManager().resetRoomTimer();
    }
}

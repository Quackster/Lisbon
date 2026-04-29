package net.h4bbo.lisbon.messages.incoming.rooms.user;

import net.h4bbo.lisbon.game.player.Player;
import net.h4bbo.lisbon.messages.types.MessageEvent;
import net.h4bbo.lisbon.server.netty.streams.NettyRequest;

public class WAVE implements MessageEvent {
    @Override
    public void handle(Player player, NettyRequest reader) {
        player.getRoomUser().wave();
        player.getRoomUser().getTimerManager().resetRoomTimer();
    }

}

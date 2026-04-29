package net.h4bbo.lisbon.messages.incoming.rooms;

import net.h4bbo.lisbon.game.player.Player;
import net.h4bbo.lisbon.messages.outgoing.rooms.ROOM_INTEREST;
import net.h4bbo.lisbon.messages.types.MessageEvent;
import net.h4bbo.lisbon.server.netty.streams.NettyRequest;

public class GETINTEREST implements MessageEvent {
    @Override
    public void handle(Player player, NettyRequest reader) {
        player.send(new ROOM_INTEREST());
    }
}

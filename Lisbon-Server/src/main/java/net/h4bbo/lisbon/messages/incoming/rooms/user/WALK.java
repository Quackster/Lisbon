package net.h4bbo.lisbon.messages.incoming.rooms.user;

import net.h4bbo.lisbon.game.player.Player;
import net.h4bbo.lisbon.messages.types.MessageEvent;
import net.h4bbo.lisbon.server.netty.streams.NettyRequest;

public class WALK implements MessageEvent {
    @Override
    public void handle(Player player, NettyRequest reader) {
        if (!player.getRoomUser().isWalkingAllowed()) {
            return;
        }

        int X = reader.readBase64();
        int Y = reader.readBase64();

        player.getRoomUser().walkTo(X, Y);
    }
}

package net.h4bbo.lisbon.messages.incoming.rooms.badges;

import net.h4bbo.lisbon.game.player.Player;
import net.h4bbo.lisbon.messages.outgoing.rooms.badges.AVAILABLE_BADGES;
import net.h4bbo.lisbon.messages.types.MessageEvent;
import net.h4bbo.lisbon.server.netty.streams.NettyRequest;

public class GETAVAILABLEBADGES implements MessageEvent {
    @Override
    public void handle(Player player, NettyRequest reader) {
        player.send(new AVAILABLE_BADGES(player.getDetails()));
    }
}
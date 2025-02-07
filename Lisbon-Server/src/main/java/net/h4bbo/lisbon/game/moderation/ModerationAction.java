package net.h4bbo.lisbon.game.moderation;

import net.h4bbo.lisbon.game.player.Player;
import net.h4bbo.lisbon.game.room.Room;
import net.h4bbo.lisbon.server.netty.streams.NettyRequest;

public interface ModerationAction {
    void performAction(Player player, Room room, String alertMessage, String notes, NettyRequest reader);
}


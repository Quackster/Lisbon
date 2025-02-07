package net.h4bbo.lisbon.messages.incoming.events;

import net.h4bbo.lisbon.game.player.Player;
import net.h4bbo.lisbon.messages.outgoing.events.ROOMEVENT_TYPES;
import net.h4bbo.lisbon.messages.types.MessageEvent;
import net.h4bbo.lisbon.server.netty.streams.NettyRequest;
import net.h4bbo.lisbon.util.config.GameConfiguration;

public class GET_ROOMEVENT_TYPE_COUNT implements MessageEvent {
    @Override
    public void handle(Player player, NettyRequest reader) throws Exception {
        player.send(new ROOMEVENT_TYPES(GameConfiguration.getInstance().getInteger("events.category.count")));
    }
}

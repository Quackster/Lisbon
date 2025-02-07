package net.h4bbo.lisbon.messages.incoming.events;

import net.h4bbo.lisbon.game.events.Event;
import net.h4bbo.lisbon.game.events.EventsManager;
import net.h4bbo.lisbon.game.player.Player;
import net.h4bbo.lisbon.messages.outgoing.events.ROOMEEVENT_INFO;
import net.h4bbo.lisbon.messages.types.MessageEvent;
import net.h4bbo.lisbon.server.netty.streams.NettyRequest;
import net.h4bbo.lisbon.util.StringUtil;

public class CREATE_ROOMEVENT implements MessageEvent {
    @Override
    public void handle(Player player, NettyRequest reader) throws Exception {
        if (!EventsManager.getInstance().canCreateEvent(player)) {
            return;
        }

        int category = reader.readInt();

        if (category < 1 || category > 11) {
            return;
        }

        String name = StringUtil.filterInput(reader.readString(), true);
        String description = StringUtil.filterInput(reader.readString(), true);

        Event event = EventsManager.getInstance().createEvent(player, category, name, description);
        player.getRoomUser().getRoom().send(new ROOMEEVENT_INFO(event));
    }
}

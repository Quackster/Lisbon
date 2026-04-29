package net.h4bbo.lisbon.messages.incoming.events;

import net.h4bbo.lisbon.game.events.Event;
import net.h4bbo.lisbon.game.events.EventsManager;
import net.h4bbo.lisbon.game.player.Player;
import net.h4bbo.lisbon.game.room.Room;
import net.h4bbo.lisbon.messages.outgoing.events.ROOMEEVENT_INFO;
import net.h4bbo.lisbon.messages.types.MessageEvent;
import net.h4bbo.lisbon.server.netty.streams.NettyRequest;

public class QUIT_ROOMEVENT implements MessageEvent {
    @Override
    public void handle(Player player, NettyRequest reader) throws Exception {
        Room room = player.getRoomUser().getRoom();

        if (room == null) {
            return;
        }

        if (!room.isOwner(player.getDetails().getId())) {
            return;
        }

        if (!EventsManager.getInstance().hasEvent(room.getId())) {
            return;
        }

        Event event = EventsManager.getInstance().getEventByRoomId(room.getId());

        if (event == null) {
            return;
        }

        EventsManager.getInstance().removeEvent(event);
        room.send(new ROOMEEVENT_INFO(null));
    }
}

package net.h4bbo.lisbon.messages.incoming.events;

import net.h4bbo.lisbon.dao.mysql.EventsDao;
import net.h4bbo.lisbon.game.events.Event;
import net.h4bbo.lisbon.game.events.EventsManager;
import net.h4bbo.lisbon.game.player.Player;
import net.h4bbo.lisbon.game.room.Room;
import net.h4bbo.lisbon.messages.outgoing.events.ROOMEEVENT_INFO;
import net.h4bbo.lisbon.messages.types.MessageEvent;
import net.h4bbo.lisbon.server.netty.streams.NettyRequest;
import net.h4bbo.lisbon.util.StringUtil;

public class EDIT_ROOMEVENT implements MessageEvent {
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

        int category = reader.readInt();

        if (category < 1 || category > 11) {
            return;
        }

        String name = StringUtil.filterInput(reader.readString(), true);
        String description = StringUtil.filterInput(reader.readString(), true);

        event.setCategoryId(category);
        event.setName(name);
        event.setDescription(description);

        room.send(new ROOMEEVENT_INFO(event));
        EventsDao.save(event);
    }
}

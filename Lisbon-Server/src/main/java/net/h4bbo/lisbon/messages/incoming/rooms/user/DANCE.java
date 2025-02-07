package net.h4bbo.lisbon.messages.incoming.rooms.user;

import net.h4bbo.lisbon.game.fuserights.Fuseright;
import net.h4bbo.lisbon.game.player.Player;
import net.h4bbo.lisbon.game.room.Room;
import net.h4bbo.lisbon.game.room.enums.StatusType;
import net.h4bbo.lisbon.messages.outgoing.rooms.user.USER_STATUSES;
import net.h4bbo.lisbon.messages.types.MessageEvent;
import net.h4bbo.lisbon.server.netty.streams.NettyRequest;
import net.h4bbo.lisbon.util.StringUtil;

import java.util.List;

public class DANCE implements MessageEvent {
    @Override
    public void handle(Player player, NettyRequest reader) {
        Room room = player.getRoomUser().getRoom();

        if (room == null) {
            return;
        }

        if (player.getRoomUser().containsStatus(StatusType.SIT) || player.getRoomUser().containsStatus(StatusType.LAY)) {
            return; // Don't allow dancing if they're sitting or laying.
        }

        String content = reader.contents();

        if (StringUtil.isNullOrEmpty(content)) {
            player.getRoomUser().setStatus(StatusType.DANCE, "");
        } else {
            if (!player.hasFuse(Fuseright.USE_CLUB_DANCE)) {
                return;
            }

            int danceId = reader.readInt();
            player.getRoomUser().setStatus(StatusType.DANCE, danceId);
        }

        player.getRoomUser().removeStatus(StatusType.CARRY_DRINK);
        player.getRoomUser().removeStatus(StatusType.CARRY_FOOD);

        if (player.getRoomUser().isWalking()) {
            player.getRoomUser().setNeedsUpdate(true);
            return;
        }

        player.getRoomUser().getRoom().send(new USER_STATUSES(List.of(player)));
        player.getRoomUser().getTimerManager().resetRoomTimer();
    }
}

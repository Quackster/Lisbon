package net.h4bbo.lisbon.messages.incoming.rooms.items;

import net.h4bbo.lisbon.game.GameScheduler;
import net.h4bbo.lisbon.game.player.Player;
import net.h4bbo.lisbon.game.room.enums.StatusType;
import net.h4bbo.lisbon.game.room.tasks.CameraTask;
import net.h4bbo.lisbon.messages.outgoing.rooms.user.USER_STATUSES;
import net.h4bbo.lisbon.messages.types.MessageEvent;
import net.h4bbo.lisbon.server.netty.streams.NettyRequest;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class USEITEM implements MessageEvent {
    @Override
    public void handle(Player player, NettyRequest reader) {
        if (!player.getRoomUser().containsStatus(StatusType.CARRY_ITEM)) {
            return;
        }
        
        String item = player.getRoomUser().getStatus(StatusType.CARRY_ITEM).getValue();

        player.getRoomUser().getStatuses().remove(StatusType.CARRY_ITEM.getStatusCode());
        player.getRoomUser().setStatus(StatusType.USE_ITEM, item);

        if (!player.getRoomUser().isWalking()) {
            player.getRoomUser().getRoom().send(new USER_STATUSES(List.of(player)));
        }

        GameScheduler.getInstance().getService().schedule(new CameraTask(player), 1, TimeUnit.SECONDS);
    }
}

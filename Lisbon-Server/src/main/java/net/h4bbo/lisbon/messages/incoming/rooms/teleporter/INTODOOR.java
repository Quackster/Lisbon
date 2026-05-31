package net.h4bbo.lisbon.messages.incoming.rooms.teleporter;

import net.h4bbo.lisbon.game.item.Item;
import net.h4bbo.lisbon.game.item.base.ItemBehaviour;
import net.h4bbo.lisbon.game.item.interactors.types.TeleportInteractor;
import net.h4bbo.lisbon.game.player.Player;
import net.h4bbo.lisbon.game.room.Room;
import net.h4bbo.lisbon.messages.types.MessageEvent;
import net.h4bbo.lisbon.server.netty.streams.NettyRequest;
import org.apache.commons.lang3.StringUtils;

public class INTODOOR implements MessageEvent {
    @Override
    public void handle(Player player, NettyRequest reader) {
        Room room = player.getRoomUser().getRoom();

        if (room == null) {
            return;
        }

        String contents = reader.contents();

        if (!StringUtils.isNumeric(contents)) {
            return;
        }

        Item item = room.getItemManager().getById(Integer.parseInt(contents));

        if (item == null || !item.hasBehaviour(ItemBehaviour.TELEPORTER)) {
            return;
        }

        if (player.getRoomUser().getAuthenticateTelporterId() != -1) {
            return;
        }


        if (!item.getPosition().touches(player.getRoomUser().getPosition())
                && !item.getPosition().equals(player.getRoomUser().getPosition())) {
            return;
        }

        new TeleportInteractor().onInteract(player, room, item, 1);
    }
}

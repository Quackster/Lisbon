package net.h4bbo.lisbon.messages.incoming.rooms.items;

import net.h4bbo.lisbon.dao.mysql.ItemDao;
import net.h4bbo.lisbon.game.fuserights.Fuseright;
import net.h4bbo.lisbon.game.item.Item;
import net.h4bbo.lisbon.game.item.base.ItemBehaviour;
import net.h4bbo.lisbon.game.player.Player;
import net.h4bbo.lisbon.game.room.Room;
import net.h4bbo.lisbon.messages.outgoing.alert.ALERT;
import net.h4bbo.lisbon.messages.outgoing.user.currencies.CREDIT_BALANCE;
import net.h4bbo.lisbon.messages.types.MessageEvent;
import net.h4bbo.lisbon.server.netty.streams.NettyRequest;

public class CONVERT_FURNI_TO_CREDITS implements MessageEvent {
    @Override
    public void handle(Player player, NettyRequest reader) {
        Room room = player.getRoomUser().getRoom();

        if (room == null) {
            return;
        }

        if (!room.isOwner(player.getDetails().getId()) && !player.hasFuse(Fuseright.ANY_ROOM_CONTROLLER)) {
            return;
        }

        int itemId = reader.readInt();

        if (itemId < 0) {
            return;
        }

        Item item = room.getItemManager().getById(itemId);

        if (item == null || !item.hasBehaviour(ItemBehaviour.REDEEMABLE)) {
            return;
        }

        // Sprite is of format CF_50_goldbar. This retrieves the 50 part
        Integer amount = Integer.parseInt(item.getDefinition().getSprite().split("_")[1]);

        // Delete item and update credits amount in one atomic operation
        int currentAmount = ItemDao.redeemCreditItem(amount, itemId, player.getDetails().getId());

        // Couldn't redeem item (database error)
        if (currentAmount == -1) {
            // TODO: find real composer for this. Maybe use error composer?
            player.send(new ALERT("Unable to redeem furniture! Contact staff or support team."));
            return;
        }

        // Notify room of item removal and set credits of player
        room.getMapping().removeItem(item);
        player.getDetails().setCredits(currentAmount);

        // Send new credit amount
        player.send(new CREDIT_BALANCE(player.getDetails().getCredits()));
    }
}
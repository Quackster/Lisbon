package net.h4bbo.lisbon.messages.incoming.trade;

import net.h4bbo.lisbon.game.entity.Entity;
import net.h4bbo.lisbon.game.entity.EntityType;
import net.h4bbo.lisbon.game.player.Player;
import net.h4bbo.lisbon.game.room.Room;
import net.h4bbo.lisbon.game.room.enums.StatusType;
import net.h4bbo.lisbon.game.room.managers.RoomTradeManager;
import net.h4bbo.lisbon.messages.types.MessageEvent;
import net.h4bbo.lisbon.server.netty.streams.NettyRequest;

public class TRADE_OPEN implements MessageEvent {
    @Override
    public void handle(Player player, NettyRequest reader) {
        Room room = player.getRoomUser().getRoom();

        if (room == null) {
            return;
        }

        if (!room.getCategory().hasAllowTrading()) {
            return;
        }

        if (player.getRoomUser().getTradePartner() != null) {
            return;
        }

        int instanceId = Integer.parseInt(reader.contents());
        Entity targetPartner = room.getEntityManager().getByInstanceId(instanceId);

        if (targetPartner == null) {
            return;
        }

        if (targetPartner.getType() != EntityType.PLAYER) {
            return;
        }

        Player tradePartner = (Player) targetPartner;

        RoomTradeManager.close(player.getRoomUser());
        RoomTradeManager.close(tradePartner.getRoomUser());

        player.getRoomUser().setStatus(StatusType.TRADE, "");
        player.getRoomUser().setNeedsUpdate(true);
        player.getRoomUser().setTradePartner(tradePartner);

        tradePartner.getRoomUser().setStatus(StatusType.TRADE, "");
        tradePartner.getRoomUser().setNeedsUpdate(true);
        tradePartner.getRoomUser().setTradePartner(player);

        RoomTradeManager.refreshWindow(player);
        RoomTradeManager.refreshWindow(tradePartner);
    }
}

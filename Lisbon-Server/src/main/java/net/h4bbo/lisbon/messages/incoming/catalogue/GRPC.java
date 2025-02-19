package net.h4bbo.lisbon.messages.incoming.catalogue;

import net.h4bbo.lisbon.dao.mysql.CurrencyDao;
import net.h4bbo.lisbon.dao.mysql.PlayerDao;
import net.h4bbo.lisbon.game.catalogue.CatalogueItem;
import net.h4bbo.lisbon.game.catalogue.CatalogueManager;
import net.h4bbo.lisbon.game.catalogue.CataloguePage;
import net.h4bbo.lisbon.game.catalogue.RareManager;
import net.h4bbo.lisbon.game.fuserights.Fuseright;
import net.h4bbo.lisbon.game.item.Item;
import net.h4bbo.lisbon.game.item.ItemManager;
import net.h4bbo.lisbon.game.player.Player;
import net.h4bbo.lisbon.game.player.PlayerDetails;
import net.h4bbo.lisbon.game.player.PlayerManager;
import net.h4bbo.lisbon.game.texts.TextsManager;
import net.h4bbo.lisbon.messages.outgoing.alert.ALERT;
import net.h4bbo.lisbon.messages.outgoing.alert.NO_USER_FOUND;
import net.h4bbo.lisbon.messages.outgoing.catalogue.NO_CREDITS;
import net.h4bbo.lisbon.messages.outgoing.rooms.items.ITEM_DELIVERED;
import net.h4bbo.lisbon.messages.outgoing.user.currencies.CREDIT_BALANCE;
import net.h4bbo.lisbon.messages.types.MessageEvent;
import net.h4bbo.lisbon.server.netty.streams.NettyRequest;
import net.h4bbo.lisbon.util.DateUtil;
import net.h4bbo.lisbon.util.StringUtil;
import net.h4bbo.lisbon.util.config.GameConfiguration;

import java.util.Optional;

public class GRPC implements MessageEvent {
    @Override
    public void handle(Player player, NettyRequest reader) throws Exception {
        String content = reader.contents();
        String[] data = content.split(Character.toString((char) 13));

        String saleCode = data[3];

        CatalogueItem item = CatalogueManager.getInstance().getCatalogueItem(saleCode);

        if (item == null) {
            return;
        }

        int price = item.getPrice();

        // If the item is not a buyable special rare, then check if they can actually buy it
        if (RareManager.getInstance().getCurrentRare() != null && item != RareManager.getInstance().getCurrentRare()) {
            Optional<CataloguePage> pageStream = CatalogueManager.getInstance().getCataloguePages().stream().filter(p -> item.hasPage(p.getId())).findFirst();

            if (!pageStream.isPresent() || pageStream.get().getMinRole().getRankId() > player.getDetails().getRank().getRankId()) {
                return;
            }
        }

        var currentRare = RareManager.getInstance().getCurrentRare();

        if (currentRare != null && currentRare == item) {
            if (!player.hasFuse(Fuseright.CREDITS)) {
                price = RareManager.getInstance().getRareCost().get(currentRare);
            }
        }

        if (price > player.getDetails().getCredits()) {
            player.send(new NO_CREDITS());
            return;
        }

        if (data[5].equals("1")) { // It's a gift!
            PlayerDetails receivingUserDetails = PlayerManager.getInstance().getPlayerData(PlayerDao.getId(data[6]));

            //if (!data[6].toLowerCase().equals(player.getDetails().getName().toLowerCase())) {
            if (receivingUserDetails == null) {
                player.send(new NO_USER_FOUND(data[6]));
                return;
            }
            //}

            String presentNote = "";
            String extraData = data[4];

            try {
                presentNote = data[7];
            } catch (Exception ignored) {
                presentNote = "";
            }

            if (presentNote.isEmpty()) {
                presentNote = " ";
            }
            
            extraData = extraData.replace(Item.PRESENT_DELIMETER, "");
            presentNote = presentNote.replace(Item.PRESENT_DELIMETER, "");

            if (item.getDefinition() != null && item.getDefinition().getSprite().equals("poster")) {
                extraData = String.valueOf(item.getItemSpecialId());
            }

            Item present = ItemManager.getInstance().createGift(receivingUserDetails.getId(), player.getDetails().getName(), item.getSaleCode(), StringUtil.filterInput(presentNote, false), extraData);//new Item();
            /*present.setOwnerId(receivingUserDetails.getId());
            present.setDefinitionId(ItemManager.getInstance().getDefinitionBySprite("present_gen" + ThreadLocalRandom.current().nextInt(1, 7)).getId());
            present.setCustomData(saleCode +
                    Item.PRESENT_DELIMETER + player.getDetails().getName() +
                    Item.PRESENT_DELIMETER + StringUtil.filterInput(presentNote, true) +
                    Item.PRESENT_DELIMETER + extraData +
                    Item.PRESENT_DELIMETER + DateUtil.getCurrentTimeSeconds());

            ItemDao.newItem(present);*/

            Player receiver = PlayerManager.getInstance().getPlayerById(receivingUserDetails.getId());

            if (receiver != null) {
                receiver.getInventory().addItem(present);
                receiver.getInventory().getView("last");
                //receiver.send(new ITEM_DELIVERED());
            }

            player.send(new ALERT(TextsManager.getInstance().getValue("successfully_purchase_gift_for").replace("%user%", receivingUserDetails.getName())));
            //player.send(new DELIVER_PRESENT(present));
        } else {
            String extraData = null;

            if (!item.isPackage()) {
                extraData = data[4];
            }

            if (CatalogueManager.getInstance().purchase(player, item, extraData, null, DateUtil.getCurrentTimeSeconds()).size() > 0) {
                player.getInventory().getView("new");
            }

            boolean showItemDelivered = true;

            // Don't send item delivered if they just buy film
            if (item.getDefinition() != null && item.getDefinition().getSprite().equals("film")) {
                showItemDelivered = false;
            }

            if (showItemDelivered) {
                if (!GameConfiguration.getInstance().getBoolean("disable.purchase.successful.alert")) {
                    player.send(new ITEM_DELIVERED());
                }
            }
        }

        CurrencyDao.decreaseCredits(player.getDetails(), price);
        player.send(new CREDIT_BALANCE(player.getDetails().getCredits()));
    }
}

package net.h4bbo.lisbon.game.commands.registered;

import net.h4bbo.lisbon.game.catalogue.CatalogueManager;
import net.h4bbo.lisbon.game.commands.Command;
import net.h4bbo.lisbon.game.entity.Entity;
import net.h4bbo.lisbon.game.entity.EntityType;
import net.h4bbo.lisbon.game.fuserights.Fuseright;
import net.h4bbo.lisbon.game.item.ItemManager;
import net.h4bbo.lisbon.game.player.Player;
import net.h4bbo.lisbon.game.player.PlayerManager;
import net.h4bbo.lisbon.game.room.models.RoomModelManager;
import net.h4bbo.lisbon.game.texts.TextsManager;
import net.h4bbo.lisbon.messages.outgoing.alert.ALERT;
import net.h4bbo.lisbon.messages.outgoing.catalogue.CATALOGUE_PAGES;
import net.h4bbo.lisbon.util.config.GameConfiguration;
import net.h4bbo.lisbon.util.config.writer.GameConfigWriter;

public class ReloadCommand extends Command {
    @Override
    public void addPermissions() {
        this.permissions.add(Fuseright.ADMINISTRATOR_ACCESS);
    }

    @Override
    public void addArguments() {
        this.arguments.add("component");
    }

    @Override
    public void handleCommand(Entity entity, String message, String[] args) {
        if (entity.getType() != EntityType.PLAYER) {
            return;
        }

        Player player = (Player) entity;

        if (player.getRoomUser().getRoom() == null) {
            return;
        }

        String component = args[0];
        String componentName = null;

        if (component.equalsIgnoreCase("catalogue")
                || component.equalsIgnoreCase("shop")
                || component.equalsIgnoreCase("items")) {
            ItemManager.reset();
            CatalogueManager.reset();

            // Regenerate collision map with proper height differences (if they changed).
            player.getRoomUser().getRoom().getMapping().regenerateCollisionMap();

            // Resend the catalogue index every 15 minutes to clear page cache
            for (Player p : PlayerManager.getInstance().getPlayers()) {
                p.send(new CATALOGUE_PAGES(
                        CatalogueManager.getInstance().getPagesForRank(p.getDetails().getRank(), p.getDetails().hasClubSubscription())
                ));
            }

            componentName = "Catalogue and item definitions";
        }

        if (component.equalsIgnoreCase("texts")) {
            TextsManager.reset();
            componentName = "Texts";
        }

        if (component.equalsIgnoreCase("models")) {
            RoomModelManager.reset();
            componentName = "Room models";
        }

        if (component.equalsIgnoreCase("settings") ||
                component.equalsIgnoreCase("config")) {

            GameConfiguration.reset(new GameConfigWriter());
            componentName = "Game settings";
        }

        if (componentName != null) {
            player.send(new ALERT(componentName + " has been reloaded."));
        } else {
            player.send(new ALERT("You did not specify which component to reload!<br>You may reload either the catalogue/shop/items, models, texts or game settings."));
        }
    }

    @Override
    public String getDescription() {
        return "Refresh the settings/items/texts";
    }
}
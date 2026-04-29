package net.h4bbo.lisbon.game.commands.registered;

import net.h4bbo.lisbon.game.commands.Command;
import net.h4bbo.lisbon.game.entity.Entity;
import net.h4bbo.lisbon.game.entity.EntityType;
import net.h4bbo.lisbon.game.fuserights.Fuseright;
import net.h4bbo.lisbon.game.player.Player;
import net.h4bbo.lisbon.messages.outgoing.alert.ALERT;

public class AboutCommand extends Command {

    @Override
    public void addPermissions() {
        this.permissions.add(Fuseright.DEFAULT);
    }

    @Override
    public void handleCommand(Entity entity, String message, String[] args) {
        if (entity.getType() != EntityType.PLAYER) {
            return;
        }

        Player player = (Player)entity;

        player.send(new ALERT("Project Lisbon - Habbo Hotel v26 emulation" +
                "<br>" +
                "<br>Max version supported: r26_20080915_0408_7984_61ccb5f8b8797a3aba62c1fa2ca80169" +
                "<br>" +
                "<br>Originally based off Kepler" +
                "<br>" +
                "<br>" +
                "Made by Quackster from RaGEZONE"));
    }

    @Override
    public String getDescription() {
        return " Information about the software powering this retro";
    }
}

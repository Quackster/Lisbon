package net.h4bbo.lisbon.game.commands.clientside;

import net.h4bbo.lisbon.game.commands.Command;
import net.h4bbo.lisbon.game.entity.Entity;
import net.h4bbo.lisbon.game.fuserights.Fuseright;

public class EventsCommand extends Command {
    @Override
    public void addPermissions() {
        this.permissions.add(Fuseright.DEFAULT);
    }

    @Override
    public void handleCommand(Entity entity, String message, String[] args) {

    }

    @Override
    public String getDescription() {
        return "Show current events organised by other users";
    }
}

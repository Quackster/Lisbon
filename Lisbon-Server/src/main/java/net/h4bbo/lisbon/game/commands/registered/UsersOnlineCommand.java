package net.h4bbo.lisbon.game.commands.registered;

import net.h4bbo.lisbon.game.commands.Command;
import net.h4bbo.lisbon.game.entity.Entity;
import net.h4bbo.lisbon.game.entity.EntityType;
import net.h4bbo.lisbon.game.fuserights.Fuseright;
import net.h4bbo.lisbon.game.player.Player;
import net.h4bbo.lisbon.game.player.PlayerManager;
import net.h4bbo.lisbon.messages.outgoing.alert.ALERT;
import net.h4bbo.lisbon.util.StringUtil;

import java.util.List;
import java.util.Map;

public class UsersOnlineCommand extends Command {
    @Override
    public void addPermissions() {
        this.permissions.add(Fuseright.DEFAULT);
    }

    @Override
    public void handleCommand(Entity entity, String message, String[] args) {
        if (entity.getType() != EntityType.PLAYER) {
            return;
        }

        int maxPlayersPerLine = 5;

        List<Player> players = PlayerManager.getInstance().getPlayers();
        Map<Integer, List<Player>> paginatedPlayers = StringUtil.paginate(players, maxPlayersPerLine);

        Player session = (Player) entity;

        StringBuilder sb = new StringBuilder()
                .append("Users online: ").append(players.size()).append("<br>")
                .append("Daily player peak count: ").append(PlayerManager.getInstance().getDailyPlayerPeak()).append("<br>")
                .append("List of users online: ").append("<br><br>");

        for (List<Player> playerList : paginatedPlayers.values()) {
            int i = 0;
            int length = playerList.size();
            for (Player player : playerList) {
                sb.append(player.getDetails().getName());

                i++;

                if (i < length) {
                    sb.append(", ");
                }
            }

            sb.append("<br>");
        }

        session.send(new ALERT(sb.toString()));
    }

    @Override
    public String getDescription() {
        return "Get the list of players currently online";
    }
}

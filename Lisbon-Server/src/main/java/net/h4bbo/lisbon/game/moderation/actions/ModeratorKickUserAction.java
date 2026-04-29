package net.h4bbo.lisbon.game.moderation.actions;

import net.h4bbo.lisbon.dao.mysql.ModerationDao;
import net.h4bbo.lisbon.game.fuserights.Fuseright;
import net.h4bbo.lisbon.game.moderation.ModerationAction;
import net.h4bbo.lisbon.game.moderation.ModerationActionType;
import net.h4bbo.lisbon.game.player.Player;
import net.h4bbo.lisbon.game.player.PlayerManager;
import net.h4bbo.lisbon.game.room.Room;
import net.h4bbo.lisbon.game.texts.TextsManager;
import net.h4bbo.lisbon.messages.outgoing.alert.ALERT;
import net.h4bbo.lisbon.messages.outgoing.moderation.MODERATOR_ALERT;
import net.h4bbo.lisbon.messages.outgoing.rooms.user.HOTEL_VIEW;
import net.h4bbo.lisbon.server.netty.streams.NettyRequest;

public class ModeratorKickUserAction implements ModerationAction {
    @Override
    public void performAction(Player player, Room room, String alertMessage, String notes, NettyRequest reader) {
        if (!player.hasFuse(Fuseright.KICK)) {
            return;
        }

        String alertUser = reader.readString();
        Player target = PlayerManager.getInstance().getPlayerByName(alertUser);

        if (target != null) {
            if (target.getDetails().getId() == player.getDetails().getId()) {
                return; // Can't kick yourself!
            }

            if (target.hasFuse(Fuseright.KICK)) {
                player.send(new ALERT(TextsManager.getInstance().getValue("modtool_rankerror")));
                return;
            }

            target.getRoomUser().kick(false);
            target.send(new HOTEL_VIEW());
            target.send(new MODERATOR_ALERT(alertMessage));

            ModerationDao.addLog(ModerationActionType.KICK_USER, player.getDetails().getId(), target.getDetails().getId(), alertMessage, notes);
        } else {
            player.send(new ALERT("Target user is not online."));
        }
    }
}

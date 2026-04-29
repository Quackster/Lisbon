package net.h4bbo.lisbon.messages.incoming.user;

import net.h4bbo.lisbon.game.player.Player;
import net.h4bbo.lisbon.messages.outgoing.user.POSSIBLE_ACHIEVEMENTS;
import net.h4bbo.lisbon.messages.types.MessageEvent;
import net.h4bbo.lisbon.server.netty.streams.NettyRequest;

import java.util.ArrayList;

public class GET_POSSIBLE_ACHIEVEMENTS implements MessageEvent {
    @Override
    public void handle(Player player, NettyRequest reader) throws Exception {
        var possibleAchievements = new ArrayList<>(player.getAchievementManager().getPossibleAchievements());

        /*
        if (!player.getGuideManager().isGuide()) {
            possibleAchievements.removeIf(ach -> ach.getName().equals("GL"));
        }*/

        player.send(new POSSIBLE_ACHIEVEMENTS(possibleAchievements));
    }
}

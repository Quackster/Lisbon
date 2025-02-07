package net.h4bbo.lisbon.messages.incoming.tutorial;

import net.h4bbo.lisbon.dao.mysql.TutorialDao;
import net.h4bbo.lisbon.game.player.Player;
import net.h4bbo.lisbon.messages.types.MessageEvent;
import net.h4bbo.lisbon.server.netty.streams.NettyRequest;

public class SET_TUTORIAL_MODE implements MessageEvent {
    @Override
    public void handle(Player player, NettyRequest reader) throws Exception {
        int tutorialMode = reader.readInt();

        if (tutorialMode != 0 && tutorialMode != 1) {
            tutorialMode = 0;
        }

        boolean finishedTutorial = (tutorialMode == 0);

        player.getDetails().setTutorialFinished(finishedTutorial);
        TutorialDao.updateTutorialMode(player.getDetails().getId(), finishedTutorial);
    }
}

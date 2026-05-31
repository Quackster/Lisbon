package net.h4bbo.lisbon.messages.incoming.tutorial;

import net.h4bbo.lisbon.game.player.Player;
import net.h4bbo.lisbon.messages.types.MessageEvent;
import net.h4bbo.lisbon.server.netty.streams.NettyRequest;
import net.h4bbo.lisbon.game.guides.GuideManager;

public class MSG_CANCEL_TUTOR_INVITATIONS implements MessageEvent {
    @Override
    public void handle(Player player, NettyRequest reader) throws Exception {
        if (!player.getGuideManager().isGuidable()) {
            return;
        }

        if (!player.getGuideManager().isWaitingForGuide()) {
            return;
        }

        player.getGuideManager().setWaitingForGuide(false);
        player.getGuideManager().setGuidable(false);
        player.getGuideManager().getInvited().clear();
        GuideManager.getInstance().tryClearTutorial(player);
    }
}

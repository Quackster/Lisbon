package net.h4bbo.lisbon.messages.outgoing.user.settings;

import net.h4bbo.lisbon.game.player.PlayerDetails;
import net.h4bbo.lisbon.messages.types.MessageComposer;
import net.h4bbo.lisbon.server.netty.streams.NettyResponse;

public class ACCOUNT_PREFERENCES extends MessageComposer {
    private final PlayerDetails details;

    public ACCOUNT_PREFERENCES(PlayerDetails details) {
        this.details = details;
    }

    @Override
    public void compose(NettyResponse response) {
        response.writeBool(this.details.getSoundSetting());
        response.writeBool(!this.details.isTutorialFinished());
    }

    @Override
    public short getHeader() {
        return 308; // "Dt": [[#tutorial_handler, #handleAccountPreferences]]
    }
}
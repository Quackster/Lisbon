package net.h4bbo.lisbon.messages.outgoing.user.settings;

import net.h4bbo.lisbon.messages.types.MessageComposer;
import net.h4bbo.lisbon.server.netty.streams.NettyResponse;

public class ACCOUNT_PREFERENCES extends MessageComposer {
    private final boolean soundEnabled;
    private final boolean hasTutorial;

    public ACCOUNT_PREFERENCES(boolean soundEnabled, boolean hasTutorial) {
        this.soundEnabled = soundEnabled;
        this.hasTutorial = hasTutorial;
    }

    @Override
    public void compose(NettyResponse response) {
        response.writeBool(this.soundEnabled);
        response.writeBool(this.hasTutorial);
    }

    @Override
    public short getHeader() {
        return 308; // "Dt": [[#tutorial_handler, #handleAccountPreferences]]
    }
}

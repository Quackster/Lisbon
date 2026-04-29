package net.h4bbo.lisbon.messages.outgoing.user.settings;

import net.h4bbo.lisbon.game.player.PlayerDetails;
import net.h4bbo.lisbon.messages.types.MessageComposer;
import net.h4bbo.lisbon.server.netty.streams.NettyResponse;

public class SOUND_SETTING extends MessageComposer {
    private final PlayerDetails details;

    public SOUND_SETTING(PlayerDetails details) {
        this.details = details;
    }

    @Override
    public void compose(NettyResponse response) {
        response.writeBool(this.details.getSoundSetting());
        response.writeInt(0); // TODO: find out why this is needed
    }

    @Override
    public short getHeader() {
        return 308; // "Dt": [[#login_handler, #handleSoundSetting]]
    }
}
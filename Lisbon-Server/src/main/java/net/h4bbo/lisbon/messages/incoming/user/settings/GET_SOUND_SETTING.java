package net.h4bbo.lisbon.messages.incoming.user.settings;

import net.h4bbo.lisbon.game.player.Player;
import net.h4bbo.lisbon.messages.outgoing.user.settings.SOUND_SETTING;
import net.h4bbo.lisbon.messages.types.MessageEvent;
import net.h4bbo.lisbon.server.netty.streams.NettyRequest;

public class GET_SOUND_SETTING implements MessageEvent {

    @Override
    public void handle(Player player, NettyRequest reader) {
        player.send(new SOUND_SETTING(player.getDetails()));
    }
}
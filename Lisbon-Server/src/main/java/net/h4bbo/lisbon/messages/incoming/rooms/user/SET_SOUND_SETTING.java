package net.h4bbo.lisbon.messages.incoming.rooms.user;

import net.h4bbo.lisbon.dao.mysql.PlayerDao;
import net.h4bbo.lisbon.game.player.Player;
import net.h4bbo.lisbon.messages.types.MessageEvent;
import net.h4bbo.lisbon.server.netty.streams.NettyRequest;

public class SET_SOUND_SETTING implements MessageEvent {
    @Override
    public void handle(Player player, NettyRequest reader) {
        boolean enabled = reader.readBoolean();
        player.getDetails().setSoundSetting(enabled);

        PlayerDao.saveSoundSetting(player.getDetails());
    }
}

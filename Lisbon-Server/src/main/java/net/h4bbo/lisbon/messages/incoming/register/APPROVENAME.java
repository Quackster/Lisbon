package net.h4bbo.lisbon.messages.incoming.register;

import net.h4bbo.lisbon.dao.mysql.PlayerDao;
import net.h4bbo.lisbon.game.player.Player;
import net.h4bbo.lisbon.messages.outgoing.register.APPROVENAMERELY;
import net.h4bbo.lisbon.messages.types.MessageEvent;
import net.h4bbo.lisbon.server.netty.streams.NettyRequest;
import net.h4bbo.lisbon.util.StringUtil;

public class APPROVENAME implements MessageEvent {
    @Override
    public void handle(Player player, NettyRequest reader) throws Exception {
        String name = StringUtil.filterInput(reader.readString(), true);
        int nameCheckCode = getNameCheckCode(name);

        player.send(new APPROVENAMERELY(nameCheckCode));
    }

    public static int getNameCheckCode(String name) {
        int nameCheckCode = 0;

        if (PlayerDao.getId(name) > 0) {
            nameCheckCode = 4;
        } else if (name.length() > 16) {
            nameCheckCode = 1;
        } else if (name.length() < 1) {
            nameCheckCode = 2;
        } else if (name.contains(" ") || !hasAllowedCharacters(name.toLowerCase(), "1234567890qwertyuiopasdfghjklzxcvbnm-+=?!@:.,$") || name.toUpperCase().contains("MOD-")) {
            nameCheckCode = 3;
        }

        return nameCheckCode;
    }

    public static boolean hasAllowedCharacters(String str, String allowedChars) {
        if (str == null) {
            return false;
        }

        for (int i = 0; i < str.length(); i++) {
            if (allowedChars.contains(Character.valueOf(str.toCharArray()[i]).toString())) {
                continue;
            }

            return false;
        }

        return true;
    }
}

package net.h4bbo.lisbon.messages.incoming.rooms.user;

import net.h4bbo.lisbon.game.commands.CommandManager;
import net.h4bbo.lisbon.game.player.Player;
import net.h4bbo.lisbon.game.room.Room;
import net.h4bbo.lisbon.messages.outgoing.rooms.user.CHAT_MESSAGE;
import net.h4bbo.lisbon.messages.outgoing.rooms.user.TYPING_STATUS;
import net.h4bbo.lisbon.messages.types.MessageEvent;
import net.h4bbo.lisbon.server.netty.streams.NettyRequest;
import net.h4bbo.lisbon.util.StringUtil;

public class SHOUT implements MessageEvent {
    @Override
    public void handle(Player player, NettyRequest reader) {
        Room room = player.getRoomUser().getRoom();

        if (room == null) {
            return;
        }

        String message = StringUtil.filterInput(reader.readString(), true);

        if (player.getRoomUser().isTyping()) {
            player.getRoomUser().setTyping(false);
            room.send(new TYPING_STATUS(player.getRoomUser().getInstanceId(), player.getRoomUser().isTyping()));
        }

        if (message.isEmpty()) {
            return;
        }

        if (CommandManager.getInstance().hasCommand(player, message)) {
            CommandManager.getInstance().invokeCommand(player, message);
            return;
        }

        player.getRoomUser().talk(message, CHAT_MESSAGE.ChatMessageType.SHOUT);
    }
}

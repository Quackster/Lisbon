package net.h4bbo.lisbon.messages.incoming.messenger;

import net.h4bbo.lisbon.dao.mysql.MessengerDao;
import net.h4bbo.lisbon.game.messenger.MessengerMessage;
import net.h4bbo.lisbon.game.messenger.MessengerUser;
import net.h4bbo.lisbon.game.player.Player;
import net.h4bbo.lisbon.game.player.PlayerManager;
import net.h4bbo.lisbon.game.wordfilter.WordfilterManager;
import net.h4bbo.lisbon.messages.outgoing.messenger.INSTANT_MESSAGE_ERROR;
import net.h4bbo.lisbon.messages.outgoing.messenger.MESSENGER_MSG;
import net.h4bbo.lisbon.messages.types.MessageEvent;
import net.h4bbo.lisbon.server.netty.streams.NettyRequest;
import net.h4bbo.lisbon.util.DateUtil;
import net.h4bbo.lisbon.util.StringUtil;

public class MESSENGER_SENDMSG implements MessageEvent {
    @Override
    public void handle(Player player, NettyRequest reader) throws Exception {
        //int amount = reader.readInt();

        /*List<Integer> friends = new ArrayList<>();

        for (int i = 0; i < amount; i++) {
            int friend_id = reader.readInt();
            friends.add(friend_id);
        }*/

        int userId = reader.readInt();

        String originalMessage = reader.readString();
        String message = WordfilterManager.filterMandatorySentence(StringUtil.filterInput(originalMessage, false));

        if (message.isBlank()) {
            return;
        }

        if (WordfilterManager.hasBannableSentence(player, originalMessage)) {
            WordfilterManager.performBan(player);
            return;
        }
/*
        if (player.isMuted()) {
            PlayerManager.getInstance().showMutedAlert(player);
            return;
        }
*/
        MessengerUser friend = player.getMessenger().getFriend(userId);

        if (friend == null) {
            player.send(new INSTANT_MESSAGE_ERROR(6, userId));
            return;
        }

        Player friendPlayer = PlayerManager.getInstance().getPlayerById(userId);

        if (friendPlayer == null) {
            player.send(new INSTANT_MESSAGE_ERROR(5, userId));
            return;
        }

        String chatMessage = friendPlayer.getDetails().isWordFilterEnabled() ? WordfilterManager.filterSentence(message) : message;
        int messageId = MessengerDao.newMessage(player.getDetails().getId(), userId, originalMessage);

        MessengerMessage msg = new MessengerMessage(
                messageId, userId, player.getDetails().getId(), DateUtil.getCurrentTimeSeconds(), chatMessage);

        friendPlayer.send(new MESSENGER_MSG(msg));
    }
}

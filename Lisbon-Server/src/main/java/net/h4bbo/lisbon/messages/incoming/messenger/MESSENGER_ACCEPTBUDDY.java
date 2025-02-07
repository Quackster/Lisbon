package net.h4bbo.lisbon.messages.incoming.messenger;

import net.h4bbo.lisbon.dao.mysql.MessengerDao;
import net.h4bbo.lisbon.dao.mysql.PlayerDao;
import net.h4bbo.lisbon.game.messenger.*;
import net.h4bbo.lisbon.game.player.Player;
import net.h4bbo.lisbon.game.player.PlayerManager;
import net.h4bbo.lisbon.messages.outgoing.messenger.ADD_BUDDY;
import net.h4bbo.lisbon.messages.outgoing.messenger.BUDDY_REQUEST_RESULT;
import net.h4bbo.lisbon.messages.types.MessageEvent;
import net.h4bbo.lisbon.server.netty.streams.NettyRequest;

import java.util.ArrayList;
import java.util.List;

public class MESSENGER_ACCEPTBUDDY implements MessageEvent {
    @Override
    public void handle(Player player, NettyRequest reader) {
        List<MessengerError> errors = new ArrayList<>();

        int amount = reader.readInt();

        for (int i = 0; i < amount; i++) {
            int userId = reader.readInt();

            MessengerUser newBuddy = player.getMessenger().getRequest(userId);

            if (newBuddy == null) {
                MessengerError error = new MessengerError(MessengerErrorType.FRIEND_REQUEST_NOT_FOUND);
                error.setCauser(newBuddy);

                errors.add(error);
                continue;
            }

            Messenger newBuddyData = MessengerManager.getInstance().getMessengerData(userId);

            if (newBuddyData == null) {
                // log warning
                continue;
            }

            if (player.getMessenger().isFriendsLimitReached()) {
                MessengerError error = new MessengerError(MessengerErrorType.FRIENDLIST_FULL);
                error.setCauser(newBuddy);

                errors.add(error);
                continue;
            }

            if (newBuddyData.isFriendsLimitReached()) {
                MessengerError error = new MessengerError(MessengerErrorType.TARGET_FRIEND_LIST_FULL);
                error.setCauser(newBuddy);

                errors.add(error);
                continue;
            }

            if (!newBuddyData.allowsFriendRequests()) {
                MessengerError error = new MessengerError(MessengerErrorType.TARGET_DOES_NOT_ACCEPT);
                error.setCauser(newBuddy);

                errors.add(error);
                continue;
            }

            MessengerDao.newFriend(player.getDetails().getId(), userId);
            MessengerDao.newFriend(userId, player.getDetails().getId());

            player.getMessenger().addFriend(newBuddy);
            player.send(new ADD_BUDDY(player, new MessengerUser(PlayerDao.getDetails(newBuddy.getUserId()))));

            Player friend = PlayerManager.getInstance().getPlayerById(userId);

            if (friend != null) {
                MessengerUser meAsBuddy = player.getMessenger().getMessengerUser();

                friend.getMessenger().addFriend(meAsBuddy);
                friend.send(new ADD_BUDDY(friend, meAsBuddy));
            }
        }

        player.send(new BUDDY_REQUEST_RESULT(errors));
    }
}

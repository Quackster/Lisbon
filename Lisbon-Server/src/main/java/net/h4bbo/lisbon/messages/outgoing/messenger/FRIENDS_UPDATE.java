package net.h4bbo.lisbon.messages.outgoing.messenger;

import net.h4bbo.lisbon.game.messenger.Messenger;
import net.h4bbo.lisbon.game.messenger.MessengerUser;
import net.h4bbo.lisbon.game.player.Player;
import net.h4bbo.lisbon.messages.types.MessageComposer;
import net.h4bbo.lisbon.server.netty.streams.NettyResponse;

import java.util.ArrayList;
import java.util.List;

public class FRIENDS_UPDATE extends MessageComposer {
    private final Messenger messenger;
    private final Player player;
    private final List<MessengerUser> friendsUpdated;

    public FRIENDS_UPDATE(Player player, Messenger messenger) {
        this.messenger = messenger;
        this.player = player;

        this.friendsUpdated = new ArrayList<>();
        this.messenger.getFriendsUpdate().drainTo(this.friendsUpdated);
    }

    @Override
    public void compose(NettyResponse response) {
        response.writeInt(this.messenger.getCategories().size());

        for (var category : this.messenger.getCategories()) {
            response.writeInt(category.getId());
            response.writeString(category.getName());
        }

        response.writeInt(this.friendsUpdated.size());

        for (MessengerUser friend : this.friendsUpdated) {
            response.writeInt(0);
            friend.serialise(this.player, response);
        }
    }

    @Override
    public short getHeader() {
        return 13; // "@M"
    }
}
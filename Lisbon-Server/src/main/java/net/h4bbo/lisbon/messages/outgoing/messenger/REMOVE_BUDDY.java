package net.h4bbo.lisbon.messages.outgoing.messenger;

import net.h4bbo.lisbon.game.messenger.MessengerUser;
import net.h4bbo.lisbon.game.player.Player;
import net.h4bbo.lisbon.messages.types.MessageComposer;
import net.h4bbo.lisbon.server.netty.streams.NettyResponse;

public class REMOVE_BUDDY extends MessageComposer {
    private final MessengerUser friend;
    private final Player player;

    public REMOVE_BUDDY(Player player, MessengerUser friend) {
        this.friend = friend;
        this.player = player;
    }

    @Override
    public void compose(NettyResponse response) {
        response.writeInt(this.player.getMessenger().getCategories().size());

        for (var category : this.player.getMessenger().getCategories()) {
            response.writeInt(category.getId());
            response.writeString(category.getName());
        }


        response.writeInt(1);
        response.writeInt(-1);
        
        response.writeInt(friend.getUserId());
    }

    @Override
    public short getHeader() {
        return 13;
    }
}

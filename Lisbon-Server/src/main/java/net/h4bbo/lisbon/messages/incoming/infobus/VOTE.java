package net.h4bbo.lisbon.messages.incoming.infobus;

import net.h4bbo.lisbon.game.infobus.InfobusManager;
import net.h4bbo.lisbon.game.player.Player;
import net.h4bbo.lisbon.messages.types.MessageEvent;
import net.h4bbo.lisbon.server.netty.streams.NettyRequest;

public class VOTE implements MessageEvent {
    @Override
    public void handle(Player player, NettyRequest reader) throws Exception {
        if (player.getRoomUser().getRoom() == null) {
            return;
        }

        String vote = reader.contents();;
        InfobusManager.getInstance().addVote(Integer.parseInt(vote));
    }
}

package net.h4bbo.lisbon.messages.outgoing.messenger;

import net.h4bbo.lisbon.game.player.Player;
import net.h4bbo.lisbon.game.player.PlayerDetails;
import net.h4bbo.lisbon.game.player.PlayerManager;
import net.h4bbo.lisbon.messages.types.MessageComposer;
import net.h4bbo.lisbon.server.netty.streams.NettyResponse;
import net.h4bbo.lisbon.util.DateUtil;

import java.util.List;

public class MESSENGER_SEARCH extends MessageComposer {
    private final List<PlayerDetails> friends;
    private final List<PlayerDetails> others;

    public MESSENGER_SEARCH(List<PlayerDetails> friends, List<PlayerDetails> others) {
        this.friends = friends;
        this.others = others;
    }

    @Override
    public void compose(NettyResponse response) {
        response.writeInt(this.friends.size());

        for (PlayerDetails playerDetails : this.friends) {
            this.serialiseSearch(response, playerDetails);
        }

        response.writeInt(this.others.size());

        for (PlayerDetails playerDetails : this.others) {
            this.serialiseSearch(response, playerDetails);
        }
    }

    private void serialiseSearch(NettyResponse response, PlayerDetails playerDetails) {
        response.writeInt(playerDetails.getId());
        response.writeString(playerDetails.getName());
        response.writeString(playerDetails.getMotto());

        Player player = PlayerManager.getInstance().getPlayerById(playerDetails.getId());
        boolean isOnline = player != null;

        response.writeBool(isOnline);
        response.writeBool(isOnline && player.getRoomUser().getRoom() != null);
        response.writeString((isOnline && player.getRoomUser().getRoom() != null) ? player.getRoomUser().getRoom().getData().getName() : "");

        response.writeBool(Character.toString(playerDetails.getSex()).equals("M"));
        response.writeString(isOnline ? playerDetails.getFigure() : "");
        response.writeString(DateUtil.getDateAsString(playerDetails.getLastOnline()));
    }

    @Override
    public short getHeader() {
        return 435; // Fs
    }
}

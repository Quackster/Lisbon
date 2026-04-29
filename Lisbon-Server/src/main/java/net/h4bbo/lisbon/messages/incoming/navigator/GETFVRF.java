package net.h4bbo.lisbon.messages.incoming.navigator;

import net.h4bbo.lisbon.game.player.Player;
import net.h4bbo.lisbon.game.room.Room;
import net.h4bbo.lisbon.game.room.RoomManager;
import net.h4bbo.lisbon.messages.outgoing.navigator.FAVOURITEROOMRESULTS;
import net.h4bbo.lisbon.messages.types.MessageEvent;
import net.h4bbo.lisbon.server.netty.streams.NettyRequest;

import java.util.List;
import java.util.stream.Collectors;

public class GETFVRF implements MessageEvent {
    @Override
    public void handle(Player player, NettyRequest reader) throws Exception {
        List<Room> favouriteRooms = RoomManager.getInstance().getFavouriteRooms(player.getDetails().getId());

        List<Room> favouritePublicRooms = favouriteRooms.stream().filter(Room::isPublicRoom).collect(Collectors.toList());
        List<Room> favouriteFlatRooms = favouriteRooms.stream().filter(room -> !room.isPublicRoom()).collect(Collectors.toList());

        player.send(new FAVOURITEROOMRESULTS(player, favouritePublicRooms,  favouriteFlatRooms));
    }
}

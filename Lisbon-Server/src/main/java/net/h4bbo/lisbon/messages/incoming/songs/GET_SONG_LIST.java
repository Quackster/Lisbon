package net.h4bbo.lisbon.messages.incoming.songs;

import net.h4bbo.lisbon.dao.mysql.SongMachineDao;
import net.h4bbo.lisbon.game.player.Player;
import net.h4bbo.lisbon.game.room.Room;
import net.h4bbo.lisbon.game.song.Song;
import net.h4bbo.lisbon.messages.outgoing.songs.SONG_LIST;
import net.h4bbo.lisbon.messages.types.MessageEvent;
import net.h4bbo.lisbon.server.netty.streams.NettyRequest;

import java.util.List;

public class GET_SONG_LIST implements MessageEvent {
    @Override
    public void handle(Player player, NettyRequest reader) {
        if (player.getRoomUser().getRoom() == null) {
            return;
        }

        Room room = player.getRoomUser().getRoom();

        if (room.getItemManager().getSoundMachine() == null) {
            return;
        }

        List<Song> songList = SongMachineDao.getSongList(room.getItemManager().getSoundMachine().getId());

        player.send(new SONG_LIST(songList));
    }
}

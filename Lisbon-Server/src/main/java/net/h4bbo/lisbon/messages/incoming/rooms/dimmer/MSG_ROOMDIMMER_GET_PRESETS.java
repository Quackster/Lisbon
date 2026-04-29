package net.h4bbo.lisbon.messages.incoming.rooms.dimmer;

import net.h4bbo.lisbon.dao.mysql.MoodlightDao;
import net.h4bbo.lisbon.game.item.Item;
import net.h4bbo.lisbon.game.player.Player;
import net.h4bbo.lisbon.game.room.Room;
import net.h4bbo.lisbon.messages.outgoing.rooms.dimmer.MOODLIGHT_PRESETS;
import net.h4bbo.lisbon.messages.types.MessageEvent;
import net.h4bbo.lisbon.server.netty.streams.NettyRequest;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;

public class MSG_ROOMDIMMER_GET_PRESETS implements MessageEvent {
    @Override
    public void handle(Player player, NettyRequest reader) {
        if (player.getRoomUser().getRoom() == null) {
            return;
        }

        Room room = player.getRoomUser().getRoom();
        Item item = room.getItemManager().getMoodlight();

        if (item == null) {
            return;
        }

        if (!MoodlightDao.containsPreset(item.getId())) {
            MoodlightDao.createPresets(item.getId());
        }

        Pair<Integer, ArrayList<String>> presetData = MoodlightDao.getPresets(item.getId());

        int currentPreset = presetData.getLeft();
        ArrayList<String> presets = presetData.getRight();

        String currentPresetData = presets.get(currentPreset - 1);

        if (!item.getCustomData().contains(currentPreset + ",")) {
            item.setCustomData("1," + currentPreset + "," + currentPresetData);
            item.save();
        }

        player.send(new MOODLIGHT_PRESETS(currentPreset, presets));
    }
}
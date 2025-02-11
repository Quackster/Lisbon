package org.alexdev.http.util;

import net.h4bbo.lisbon.dao.mysql.RoomDao;

public class HousekeepingUtil {
    public String getRoomName(int roomId) {
        var room = RoomDao.getRoomById(roomId);

        if (room == null) {
            return "ERROR";
        }

        return room.getData().getName();
    }

    public String formatNewsStory(String fullstory) {
        return BBCode.format(HtmlUtil.escape(BBCode.normalise(fullstory)), true);
    }
}

package net.h4bbo.lisbon.game.achievements.progressions;

import net.h4bbo.lisbon.game.achievements.AchievementInfo;
import net.h4bbo.lisbon.game.achievements.AchievementProgress;
import net.h4bbo.lisbon.game.achievements.user.UserAchievement;
import net.h4bbo.lisbon.game.player.Player;
import net.h4bbo.lisbon.game.player.statistics.PlayerStatistic;

import java.util.concurrent.TimeUnit;

public class AchievementAllTimeHotelPresence implements AchievementProgress {
    @Override
    public boolean tryProgress(Player player, UserAchievement userAchievement, AchievementInfo achievementInfo) {
        int daysSince = (int) Math.floor(TimeUnit.SECONDS.toHours(player.getStatisticManager().getIntValue(PlayerStatistic.ONLINE_TIME)));//AchievementDao.getOnlineTime(player.getDetails().getId()))));

        if (daysSince >= achievementInfo.getProgressRequired()) {
            userAchievement.setProgress(achievementInfo.getProgressRequired());
            return true;
        }

        return false;
    }
}

package net.h4bbo.lisbon.game.achievements.progressions;

import net.h4bbo.lisbon.game.achievements.AchievementInfo;
import net.h4bbo.lisbon.game.achievements.AchievementProgress;
import net.h4bbo.lisbon.game.achievements.user.UserAchievement;
import net.h4bbo.lisbon.game.player.Player;
import net.h4bbo.lisbon.game.player.statistics.PlayerStatistic;
import net.h4bbo.lisbon.util.DateUtil;

import java.util.concurrent.TimeUnit;

public class AchievementRegistrationDuration implements AchievementProgress {
    @Override
    public boolean tryProgress(Player player, UserAchievement userAchievement, AchievementInfo achievementInfo) {
        int daysSinceJoined = (int) Math.floor(TimeUnit.SECONDS.toDays((long) (DateUtil.getCurrentTimeSeconds() - Math.floor(player.getDetails().getJoinDate()))));

        if (daysSinceJoined >= achievementInfo.getProgressRequired()) {
            userAchievement.setProgress(achievementInfo.getProgressRequired());
            return true;
        }

        return false;
    }
}

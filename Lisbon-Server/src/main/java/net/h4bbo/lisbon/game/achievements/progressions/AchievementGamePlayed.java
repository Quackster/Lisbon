package net.h4bbo.lisbon.game.achievements.progressions;

import net.h4bbo.lisbon.game.achievements.AchievementInfo;
import net.h4bbo.lisbon.game.achievements.AchievementProgress;
import net.h4bbo.lisbon.game.achievements.user.UserAchievement;
import net.h4bbo.lisbon.game.player.Player;
import net.h4bbo.lisbon.game.player.statistics.PlayerStatistic;

public class AchievementGamePlayed implements AchievementProgress {
    @Override
    public boolean tryProgress(Player player, UserAchievement userAchievement, AchievementInfo achievementInfo) {
        int progress = player.getStatisticManager().getIntValue(PlayerStatistic.BATTLEBALL_GAMES_WON) + player.getStatisticManager().getIntValue(PlayerStatistic.SNOWSTORM_GAMES_WON);

        if (progress >= userAchievement.getProgress()) {
            if (progress > achievementInfo.getProgressRequired()) {
                progress = achievementInfo.getProgressRequired();
            }

            userAchievement.setProgress(progress);
            return true;
        }

        return false;
    }
}

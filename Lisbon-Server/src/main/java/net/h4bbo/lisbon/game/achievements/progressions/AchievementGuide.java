package net.h4bbo.lisbon.game.achievements.progressions;

import net.h4bbo.lisbon.game.achievements.AchievementInfo;
import net.h4bbo.lisbon.game.achievements.AchievementProgress;
import net.h4bbo.lisbon.game.achievements.user.UserAchievement;
import net.h4bbo.lisbon.game.player.Player;
import net.h4bbo.lisbon.game.player.statistics.PlayerStatistic;

public class AchievementGuide implements AchievementProgress {
    @Override
    public boolean tryProgress(Player player, UserAchievement userAchievement, AchievementInfo achievementInfo) {
        int progress = player.getStatisticManager().getIntValue(PlayerStatistic.PLAYERS_GUIDED);

        if (progress >= userAchievement.getProgress()) {
            userAchievement.setProgress(progress);
            return true;
        }

        return false;
    }
}

package net.h4bbo.lisbon.game.achievements.progressions;

import net.h4bbo.lisbon.game.achievements.AchievementInfo;
import net.h4bbo.lisbon.game.achievements.AchievementProgress;
import net.h4bbo.lisbon.game.achievements.user.UserAchievement;
import net.h4bbo.lisbon.game.player.Player;
import net.h4bbo.lisbon.game.player.statistics.PlayerStatistic;

public class AchievementEmailVerification implements AchievementProgress {
    @Override
    public boolean tryProgress(Player player, UserAchievement userAchievement, AchievementInfo achievementInfo) {
        return false;
    }
}

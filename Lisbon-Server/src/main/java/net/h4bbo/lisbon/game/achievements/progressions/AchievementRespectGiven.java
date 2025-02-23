package net.h4bbo.lisbon.game.achievements.progressions;

import net.h4bbo.lisbon.game.achievements.AchievementInfo;
import net.h4bbo.lisbon.game.achievements.AchievementProgress;
import net.h4bbo.lisbon.game.achievements.user.UserAchievement;
import net.h4bbo.lisbon.game.player.Player;

public class AchievementRespectGiven implements AchievementProgress {
    @Override
    public boolean tryProgress(Player player, UserAchievement userAchievement, AchievementInfo achievementInfo) {
        userAchievement.setProgress(userAchievement.getProgress() + 1);
        return true;
    }
}

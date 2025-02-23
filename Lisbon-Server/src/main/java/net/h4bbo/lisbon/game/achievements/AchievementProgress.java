package net.h4bbo.lisbon.game.achievements;

import net.h4bbo.lisbon.game.achievements.user.UserAchievement;
import net.h4bbo.lisbon.game.player.Player;

public interface AchievementProgress {
    boolean tryProgress(Player player, UserAchievement userAchievement, AchievementInfo achievementInfo);
}

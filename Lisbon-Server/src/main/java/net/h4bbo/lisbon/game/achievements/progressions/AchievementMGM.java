package net.h4bbo.lisbon.game.achievements.progressions;

import net.h4bbo.lisbon.dao.mysql.ReferredDao;
import net.h4bbo.lisbon.game.achievements.AchievementInfo;
import net.h4bbo.lisbon.game.achievements.AchievementProgress;
import net.h4bbo.lisbon.game.achievements.user.UserAchievement;
import net.h4bbo.lisbon.game.player.Player;

public class AchievementMGM implements AchievementProgress {
    @Override
    public boolean tryProgress(Player player, UserAchievement userAchievement, AchievementInfo achievementInfo) {
        int progress = ReferredDao.countReferred(player.getDetails().getId());

        if (progress > achievementInfo.getProgressRequired()) {
            progress = achievementInfo.getProgressRequired();
        }

        if (progress != userAchievement.getProgress()) {
            userAchievement.setProgress(progress);
            return true;
        }

        return false;
    }
}

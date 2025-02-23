package net.h4bbo.lisbon.game.achievements.progressions;

import net.h4bbo.lisbon.dao.mysql.TagDao;
import net.h4bbo.lisbon.game.achievements.AchievementInfo;
import net.h4bbo.lisbon.game.achievements.AchievementProgress;
import net.h4bbo.lisbon.game.achievements.user.UserAchievement;
import net.h4bbo.lisbon.game.player.Player;

public class AchievementTags implements AchievementProgress {
    @Override
    public boolean tryProgress(Player player, UserAchievement userAchievement, AchievementInfo achievementInfo) {
        var tagList = TagDao.getUserTags(player.getDetails().getId());

        int progress = tagList.size();

        if (progress >= 5) {
            progress = achievementInfo.getProgressRequired();
        }

        if (progress >= achievementInfo.getProgressRequired()) {
            userAchievement.setProgress(progress);
            return true;
        }


        return false;
    }
}
package net.h4bbo.lisbon.game.achievements.progressions;

import net.h4bbo.lisbon.game.achievements.AchievementInfo;
import net.h4bbo.lisbon.game.achievements.AchievementProgress;
import net.h4bbo.lisbon.game.achievements.user.UserAchievement;
import net.h4bbo.lisbon.game.player.Player;
import net.h4bbo.lisbon.util.config.GameConfiguration;

public class AchievementTraderPass implements AchievementProgress {
    @Override
    public boolean tryProgress(Player player, UserAchievement userAchievement, AchievementInfo achievementInfo) {
        /*var canUseTrade = true;TimeUnit.SECONDS.toDays(DateUtil.getCurrentTimeSeconds() - player.getDetails().getJoinDate()) >= 3 &&
                player.getStatisticManager().getIntValue(PlayerStatistic.ONLINE_TIME) >= TimeUnit.MINUTES.toHours(60) && player.getDetails().isTradeEnabled();*/

        if (player.getDetails().isTradeEnabled()/* && isActivated(player.getStatisticManager().getValue(PlayerStatistic.ACTIVATION_CODE))*/) {
            userAchievement.setProgress(achievementInfo.getProgressRequired());
            return true;
        }

        return false;
    }

    public static boolean isActivated(String activationCode) {
        if (!GameConfiguration.getInstance().getBoolean("email.smtp.enable")) {
            return true;
        }

        return activationCode == null;
    }

}

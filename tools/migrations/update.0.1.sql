
CREATE TABLE IF NOT EXISTS `games_played_history` (
  `id` varchar(255) NOT NULL,
  `game_name` text NOT NULL DEFAULT '',
  `game_creator` int(11) NOT NULL,
  `game_type` varchar(50) NOT NULL DEFAULT '',
  `map_id` int(11) NOT NULL,
  `winning_team` int(11) NOT NULL,
  `winning_team_score` int(11) NOT NULL,
  `extra_data` text NOT NULL,
  `team_data` text NOT NULL,
  `played_at` datetime NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

CREATE TABLE IF NOT EXISTS `users_statistics` (
  `user_id` int(11) NOT NULL,
  `days_logged_in_row` int(11) NOT NULL DEFAULT 0,
  `guestbook_unread_messages` int(11) NOT NULL DEFAULT 0,
  `online_time` int(11) NOT NULL DEFAULT 0,
  `battleball_score_month` int(11) NOT NULL DEFAULT 0,
  `battleball_score_all_time` int(11) NOT NULL DEFAULT 0,
  `snowstorm_score_month` int(11) NOT NULL DEFAULT 0,
  `snowstorm_score_all_time` int(11) NOT NULL DEFAULT 0,
  `wobble_squabble_score_month` int(11) NOT NULL DEFAULT 0,
  `wobble_squabble_score_all_time` int(11) NOT NULL DEFAULT 0,
  `xp_earned_month` int(11) NOT NULL DEFAULT 0,
  `xp_all_time` int(11) NOT NULL DEFAULT 0,
  `battleball_games_won` int(11) NOT NULL DEFAULT 0,
  `snowstorm_games_won` int(11) NOT NULL DEFAULT 0,
  `wobble_squabble_games_won` int(11) NOT NULL DEFAULT 0,
  `guided_by` int(11) NOT NULL DEFAULT 0,
  `has_tutorial` int(11) NOT NULL DEFAULT 1,
  `players_guided` int(11) NOT NULL DEFAULT 0,
  `newbie_room_layout` int(11) NOT NULL DEFAULT 0,
  `newbie_gift` int(11) NOT NULL DEFAULT 0,
  `newbie_gift_time` bigint(11) NOT NULL DEFAULT 0,
  `club_gift_due` datetime DEFAULT NULL,
  `gifts_due` int(11) NOT NULL DEFAULT 0,
  `club_member_time` bigint(11) NOT NULL DEFAULT 0,
  `club_member_time_updated` bigint(11) NOT NULL DEFAULT 0,
  `activation_code` varchar(255) DEFAULT NULL,
  `forgot_password_code` varchar(255) DEFAULT NULL,
  `forgot_recovery_requested_time` bigint(11) DEFAULT NULL,
  `is_guidable` int(11) NOT NULL DEFAULT 1,
  `mute_expires_at` bigint(11) NOT NULL DEFAULT 0,
  KEY `activation_code` (`activation_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;


UPDATE users SET last_online = UNIX_TIMESTAMP() WHERE last_online = 0;

ALTER TABLE users ADD COLUMN last_online_datetime DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP() AFTER `updated_at`;

UPDATE users SET last_online_datetime = FROM_UNIXTIME(last_online);

ALTER TABLE users DROP COLUMN last_online;

ALTER TABLE users CHANGE last_online_datetime last_online DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP() AFTER `updated_at`;

ALTER TABLE `users`	ADD COLUMN `machine_id` TEXT NULL DEFAULT '' COLLATE 'utf8mb4_general_ci' AFTER `sso_ticket`;
ALTER TABLE `users`	ADD COLUMN `selected_room_id` INT NOT NULL DEFAULT '0' AFTER `sound_enabled`;
ALTER TABLE `users`	ADD COLUMN `online_status_visible` TINYINT(1) NOT NULL DEFAULT '1' AFTER `allow_friend_requests`;
ALTER TABLE `users` ADD COLUMN `profile_visible` TINYINT(1) NOT NULL DEFAULT '1' AFTER `online_status_visible`;
ALTER TABLE `users` ADD COLUMN `wordfilter_enabled` TINYINT(1) NOT NULL DEFAULT '1' AFTER `profile_visible`;
ALTER TABLE `users`	ADD COLUMN `trade_enabled` TINYINT(1) NOT NULL DEFAULT '1' AFTER `wordfilter_enabled`;
ALTER TABLE `users`	ADD COLUMN `trade_ban_expiration` BIGINT(20) NOT NULL DEFAULT '0' AFTER `trade_enabled`;
ALTER TABLE `users` ADD COLUMN `is_online` TINYINT NOT NULL DEFAULT 0 AFTER `last_online`;
ALTER TABLE `users` ADD COLUMN `favourite_group` INT NOT NULL DEFAULT 0 AFTER `tutorial_finished`;

ALTER TABLE `users_bans`
	ADD COLUMN `banned_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP() AFTER `banned_until`,
	ADD COLUMN `banned_by` INT NOT NULL AFTER `banned_at`,
	ADD COLUMN `is_active` TINYINT NOT NULL DEFAULT '1' AFTER `banned_by`;

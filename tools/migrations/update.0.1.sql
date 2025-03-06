CREATE TABLE IF NOT EXISTS `users_transactions` (
  `user_id` int(11) NOT NULL,
  `item_id` longtext NOT NULL,
  `catalogue_id` longtext NOT NULL,
  `amount` int(11) NOT NULL,
  `description` longtext NOT NULL DEFAULT '',
  `credit_cost` int(11) NOT NULL,
  `pixel_cost` int(11) NOT NULL,
  `created_at` datetime NOT NULL DEFAULT current_timestamp(),
  `is_visible` tinyint(1) NOT NULL DEFAULT 1,
  KEY `user_id` (`user_id`),
  KEY `created_at` (`created_at`),
  KEY `is_visible` (`is_visible`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;


ALTER TABLE `rooms`
	ADD COLUMN `group_id` INT(11) NOT NULL DEFAULT '0' AFTER `rating`;


CREATE TABLE IF NOT EXISTS `rooms_entry_badges` (
  `room_id` int(11) NOT NULL,
  `badge` varchar(15) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

DELETE FROM `rooms_entry_badges`;

CREATE TABLE IF NOT EXISTS `wordfilter` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `word` varchar(100) NOT NULL,
  `is_bannable` int(11) NOT NULL DEFAULT 0,
  `is_filterable` int(11) NOT NULL DEFAULT 1,
  PRIMARY KEY (`id`),
  UNIQUE KEY `word` (`word`)
) ENGINE=InnoDB AUTO_INCREMENT=131 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

DELETE FROM `wordfilter`;
INSERT INTO `wordfilter` (`id`, `word`, `is_bannable`, `is_filterable`) VALUES
	(1, 'aaron', 0, 1),
	(2, 'anal', 0, 1),
	(3, 'anus', 0, 1),
	(4, 'arse', 0, 1),
	(5, 'ass fuck', 0, 1),
	(6, 'ass hole', 0, 1),
	(7, 'assfucker', 0, 1),
	(8, 'asshole', 0, 1),
	(9, 'assshole', 0, 1),
	(10, 'bastard', 0, 1),
	(11, 'bitch', 0, 1),
	(12, 'black cock', 0, 1),
	(14, 'boong', 0, 1),
	(15, 'cockfucker', 0, 1),
	(16, 'cocksuck', 0, 1),
	(17, 'cocksucker', 0, 1),
	(18, 'coon', 0, 1),
	(19, 'coonnass', 0, 1),
	(20, 'crap', 0, 1),
	(21, 'cunt', 0, 0),
	(22, 'cyberfuck', 0, 1),
	(23, 'dick', 0, 1),
	(24, 'douche', 0, 1),
	(25, 'erect', 0, 1),
	(26, 'erection', 0, 1),
	(27, 'erotic', 0, 1),
	(28, 'escort', 0, 1),
	(29, 'fag', 0, 0),
	(30, 'faggot', 0, 0),
	(31, 'fuck', 0, 1),
	(32, 'Fuck off', 0, 1),
	(33, 'fuck you', 0, 1),
	(34, 'fuckass', 0, 1),
	(35, 'fuckhole', 0, 1),
	(36, 'fuckwit', 0, 1),
	(37, 'god damn', 0, 1),
	(38, 'goddamn', 0, 1),
	(39, 'gook', 0, 1),
	(40, 'h4bb0 id', 1, 0),
	(41, 'h4bb0 ld', 1, 0),
	(42, 'h4bb0,id', 1, 0),
	(43, 'h4bb0.1d', 1, 0),
	(44, 'h4bb0.id', 1, 0),
	(45, 'h4bb0.ld', 1, 0),
	(46, 'h4bbo 1d', 1, 0),
	(47, 'h4bbo id', 1, 0),
	(48, 'h4bbo.1d', 1, 0),
	(49, 'h4bbo.id', 1, 0),
	(50, 'h4bbo.ld', 1, 0),
	(51, 'h4bbo:1d', 1, 0),
	(52, 'h4bbo:id', 1, 0),
	(53, 'habb0 id', 1, 0),
	(54, 'habb0 ld', 1, 0),
	(55, 'habb0.d', 1, 0),
	(56, 'habb0.id', 1, 0),
	(57, 'habb0.Â¡d', 1, 0),
	(58, 'habbb0 id', 1, 0),
	(59, 'habbb0 ld', 1, 0),
	(60, 'habbb0.id', 1, 0),
	(61, 'habbbo . Â¡d', 1, 0),
	(62, 'habbbo .Â¡d', 1, 0),
	(63, 'habbbo id', 1, 0),
	(64, 'habbbo.Â¡d', 1, 0),
	(65, 'habbboid', 1, 0),
	(66, 'habbbold', 1, 0),
	(67, 'habbo .id', 1, 0),
	(68, 'habbo 1d', 1, 0),
	(69, 'habbo dot id', 1, 0),
	(70, 'habbo id', 1, 0),
	(71, 'habbo ld', 1, 0),
	(72, 'habbo Â¡d', 1, 0),
	(73, 'habbo(.)id', 1, 0),
	(74, 'habbo,id', 1, 0),
	(75, 'habbo,ld', 1, 0),
	(76, 'habbo. id', 1, 0),
	(77, 'habbo.1d', 1, 0),
	(78, 'habbo.id', 1, 0),
	(79, 'habbo.ld', 1, 0),
	(80, 'habbo.Â¡d', 1, 0),
	(81, 'habbo:id', 1, 0),
	(82, 'habbo:ld', 1, 0),
	(83, 'habboid', 1, 0),
	(84, 'habbold', 1, 0),
	(85, 'hard core', 0, 1),
	(86, 'hardcore', 0, 1),
	(87, 'haÎ²Î²o id', 1, 0),
	(88, 'haÎ²Î²o,id', 1, 0),
	(89, 'haÎ²Î²o.id', 1, 0),
	(90, 'haÎ²Î²o:id', 1, 0),
	(91, 'haÎ²Î²oid', 1, 0),
	(92, 'homoerotic', 0, 1),
	(93, 'hore', 0, 1),
	(94, 'mother fucker', 0, 1),
	(95, 'motherfuck', 0, 1),
	(96, 'motherfucker', 0, 1),
	(97, 'nigger', 0, 0),
	(98, 'orgasim', 0, 1),
	(99, 'orgasm', 0, 1),
	(100, 'penis', 0, 1),
	(101, 'penisfucker', 0, 1),
	(102, 'piss', 0, 1),
	(103, 'piss off', 0, 1),
	(104, 'porn', 0, 1),
	(105, 'porno', 0, 1),
	(106, 'pornography', 0, 1),
	(107, 'pussy', 0, 1),
	(108, 'retard', 0, 1),
	(109, 'sadist', 0, 1),
	(111, 'sexy', 0, 1),
	(112, 'shit', 0, 1),
	(113, 'slut', 0, 1),
	(114, 'sojobo', 0, 1),
	(115, 'son of a bitch', 0, 1),
	(116, 'tits', 0, 1),
	(117, 'viagra', 0, 1),
	(118, 'whore', 0, 1),
	(119, 'zaphotel', 0, 0),
	(120, 'habfun', 1, 0),
	(121, 'hretro', 1, 0),
	(122, 'habme.net', 1, 0),
	(124, 'habm e.net', 1, 0),
	(125, '400+ daily players // free hc // free creds+diamonds', 1, 0),
	(126, 'h abme.net', 1, 0),
	(127, 'ha bme.net', 1, 0),
	(128, 'hab me.net', 1, 0),
	(129, 'habme. net', 1, 0),
	(130, 'habme .net', 1, 0);


CREATE TABLE IF NOT EXISTS `rooms_ads` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `is_loading_ad` tinyint(1) NOT NULL DEFAULT 0,
  `room_id` int(11) NOT NULL,
  `url` varchar(255) DEFAULT NULL,
  `image` mediumtext NOT NULL,
  `enabled` tinyint(1) NOT NULL DEFAULT 1,
  KEY `room_ad id` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=82 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

DELETE FROM `rooms_ads`;
INSERT INTO `rooms_ads` (`id`, `is_loading_ad`, `room_id`, `url`, `image`, `enabled`) VALUES
	(1, 0, 1, 'http://classichabbo.com/credits/collectables', 'http://alex-dev.org/ads/billboards/billboard_collectibles_01.gif', 1),
	(2, 0, 9, NULL, 'http://alex-dev.org/ads/billboards/billboard_diner_01.gif', 1),
	(3, 0, 12, NULL, 'http://alex-dev.org/ads/billboards/billboard_idol_02.gif', 1),
	(4, 0, 13, NULL, 'http://alex-dev.org/ads/billboards/ad_rooftoptgt_outside_L.gif', 1),
	(5, 0, 14, NULL, 'http://alex-dev.org/ads/billboards/ad_rooftoptgt_inside_R.gif', 1),
	(6, 0, 36, NULL, 'http://alex-dev.org/ads/billboards/ad_lido_L.gif', 1),
	(60, 1, -1, NULL, 'http://cdn.classichabbo.com/c_images/room_ads/hc.gif', 1),
	(61, 1, -1, NULL, 'http://cdn.classichabbo.com/c_images/room_ads/ai_1.gif', 1),
	(62, 1, -1, NULL, 'http://cdn.classichabbo.com/c_images/room_ads/country.gif', 1),
	(63, 1, -1, NULL, 'http://cdn.classichabbo.com/c_images/room_ads/diner.gif', 1),
	(64, 1, -1, NULL, 'http://cdn.classichabbo.com/c_images/room_ads/interstitial_hc.gif', 1),
	(65, 1, -1, NULL, 'http://cdn.classichabbo.com/c_images/room_ads/interstitial_hcpromo09_hcparty3.gif', 1),
	(66, 1, -1, NULL, 'http://cdn.classichabbo.com/c_images/room_ads/interstitial_hween09.gif', 1),
	(67, 1, -1, NULL, 'http://cdn.classichabbo.com/c_images/room_ads/interstitial_kitchen.gif', 1),
	(68, 1, -1, NULL, 'http://cdn.classichabbo.com/c_images/room_ads/interstitial_pay2playscam.gif', 1),
	(69, 1, -1, NULL, 'http://cdn.classichabbo.com/c_images/room_ads/roomloadtrophies.gif', 1),
	(70, 1, -1, NULL, 'http://cdn.classichabbo.com/c_images/room_ads/roomloadpixels.gif', 1),
	(71, 1, -1, NULL, 'http://cdn.classichabbo.com/c_images/room_ads/roomloadbobba.gif', 1),
	(72, 1, -1, NULL, 'http://cdn.classichabbo.com/c_images/room_ads/mall.gif', 1),
	(73, 1, -1, NULL, 'http://cdn.classichabbo.com/c_images/room_ads/love_earth.gif', 1),
	(74, 1, -1, NULL, 'http://cdn.classichabbo.com/c_images/room_ads/loadingscreen.gif', 1),
	(75, 1, -1, NULL, 'http://cdn.classichabbo.com/c_images/room_ads/jungle.gif', 1),
	(76, 1, -1, NULL, 'http://cdn.classichabbo.com/c_images/room_ads/it_bolly.gif', 1),
	(77, 1, -1, NULL, 'http://cdn.classichabbo.com/c_images/room_ads/safety_148.gif', 1),
	(78, 1, -1, NULL, 'http://cdn.classichabbo.com/c_images/room_ads/trophies.gif', 1),
	(79, 1, -1, NULL, 'http://cdn.classichabbo.com/c_images/room_ads/us_ying_yang_bb.gif', 1),
	(80, 1, -1, NULL, 'http://cdn.classichabbo.com/c_images/room_ads/windows.gif', 1),
	(81, 1, -1, NULL, 'http://cdn.classichabbo.com/c_images/room_ads/xmas.gif', 0);


CREATE TABLE IF NOT EXISTS `catalogue_collectables` (
  `store_page` int(11) NOT NULL,
  `admin_page` int(11) NOT NULL,
  `expiry` bigint(11) NOT NULL,
  `lifetime` bigint(11) NOT NULL DEFAULT 2678400,
  `current_position` int(11) NOT NULL,
  `class_names` text NOT NULL,
  PRIMARY KEY (`store_page`),
  UNIQUE KEY `store_page` (`store_page`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

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

CREATE TABLE IF NOT EXISTS `messenger_categories` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `name` varchar(50) NOT NULL,
  KEY `Index 1` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

ALTER TABLE `messenger_friends`
	ADD COLUMN `category_id` INT NULL AFTER `to_id`,
	ADD INDEX `from_id` (`from_id`),
	ADD INDEX `to_id` (`to_id`),
	ADD INDEX `category_id` (`category_id`);
	
DROP TABLE IF EXISTS `messenger_categories`;
CREATE TABLE IF NOT EXISTS `messenger_categories` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `name` varchar(50) NOT NULL,
  KEY `Index 1` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

DROP TABLE IF EXISTS `achievements`;
CREATE TABLE IF NOT EXISTS `achievements` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `achievement` varchar(64) NOT NULL,
  `level` int(11) NOT NULL DEFAULT 1,
  `reward_pixels` int(11) NOT NULL DEFAULT 0,
  `progress_needed` int(11) NOT NULL DEFAULT 1,
  `disabled` int(11) NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=54 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

DELETE FROM `achievements`;
INSERT INTO `achievements` (`id`, `achievement`, `level`, `reward_pixels`, `progress_needed`, `disabled`) VALUES
	(1, 'ACH_Motto', 1, 10, 1, 0),
	(2, 'ACH_AvatarLooks', 1, 50, 1, 0),
	(3, 'ACH_Student', 1, 20, 1, 1),
	(4, 'ACH_EmailVerification', 1, 200, 1, 1),
	(5, 'ACH_Graduate', 1, 20, 1, 0),
	(6, 'ACH_HappyHour', 1, 100, 1, 0),
	(7, 'HC', 1, 100, 1, 0),
	(8, 'HC', 2, 200, 12, 0),
	(9, 'HC', 3, 300, 24, 0),
	(10, 'ACH_Login', 1, 50, 5, 0),
	(11, 'ACH_Login', 2, 80, 8, 0),
	(12, 'ACH_Login', 3, 120, 15, 0),
	(13, 'ACH_Login', 4, 150, 28, 0),
	(14, 'ACH_Login', 5, 200, 35, 0),
	(15, 'ACH_Login', 6, 200, 60, 0),
	(16, 'ACH_Login', 7, 200, 70, 0),
	(17, 'ACH_Login', 8, 200, 80, 0),
	(18, 'ACH_Login', 9, 200, 90, 0),
	(19, 'ACH_Login', 10, 200, 100, 0),
	(20, 'ACH_RegistrationDuration', 1, 30, 3, 0),
	(21, 'ACH_RegistrationDuration', 2, 60, 21, 0),
	(22, 'ACH_RegistrationDuration', 3, 90, 56, 0),
	(23, 'ACH_RegistrationDuration', 4, 120, 112, 0),
	(24, 'ACH_RegistrationDuration', 5, 160, 168, 0),
	(25, 'ACH_RegistrationDuration', 6, 200, 365, 0),
	(26, 'ACH_RegistrationDuration', 7, 200, 730, 0),
	(27, 'ACH_RegistrationDuration', 8, 200, 1095, 0),
	(28, 'ACH_RegistrationDuration', 9, 200, 1461, 0),
	(29, 'ACH_RegistrationDuration', 10, 200, 1826, 0),
	(30, 'ACH_RoomEntry', 1, 10, 5, 0),
	(31, 'ACH_RoomEntry', 2, 10, 15, 0),
	(32, 'ACH_RoomEntry', 3, 15, 30, 0),
	(33, 'ACH_RoomEntry', 4, 15, 50, 0),
	(34, 'ACH_RoomEntry', 5, 15, 60, 0),
	(35, 'ACH_RoomEntry', 6, 20, 80, 0),
	(36, 'ACH_RoomEntry', 7, 20, 120, 0),
	(37, 'ACH_RoomEntry', 8, 30, 140, 0),
	(38, 'ACH_RoomEntry', 9, 30, 160, 0),
	(39, 'ACH_RoomEntry', 10, 40, 200, 0),
	(40, 'HC', 4, 400, 36, 0),
	(41, 'HC', 5, 500, 48, 0),
	(42, 'ACH_GamePlayed', 1, 10, 1, 0),
	(43, 'ACH_GamePlayed', 2, 30, 5, 0),
	(44, 'ACH_GamePlayed', 3, 50, 20, 0),
	(45, 'ACH_GamePlayed', 4, 80, 50, 0),
	(46, 'ACH_GamePlayed', 5, 100, 100, 0),
	(47, 'ACH_GamePlayed', 6, 120, 160, 0),
	(48, 'ACH_GamePlayed', 7, 160, 200, 0),
	(49, 'ACH_GamePlayed', 8, 220, 280, 0),
	(50, 'ACH_GamePlayed', 9, 280, 360, 0),
	(51, 'ACH_GamePlayed', 10, 340, 440, 0),
	(52, 'ACH_Student', 1, 20, 1, 0),
	(53, 'ACH_EmailVerification', 1, 200, 1, 0);

DROP TABLE IF EXISTS `users_achievements`;
CREATE TABLE IF NOT EXISTS `users_achievements` (
  `achievement_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `progress` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

DROP TABLE IF EXISTS `groups_details`;
CREATE TABLE IF NOT EXISTS `groups_details` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  `description` mediumtext NOT NULL,
  `owner_id` int(10) NOT NULL,
  `room_id` int(10) NOT NULL DEFAULT 0,
  `badge` mediumtext NOT NULL DEFAULT 'b0503Xs09114s05013s05015',
  `recommended` int(1) NOT NULL DEFAULT 0,
  `background` varchar(255) NOT NULL DEFAULT 'bg_colour_08',
  `views` int(15) NOT NULL DEFAULT 0,
  `topics` smallint(1) NOT NULL DEFAULT 0,
  `group_type` tinyint(3) unsigned NOT NULL DEFAULT 0,
  `forum_type` tinyint(1) unsigned NOT NULL DEFAULT 0,
  `forum_premission` tinyint(1) unsigned NOT NULL DEFAULT 0,
  `alias` varchar(30) DEFAULT NULL,
  `created_at` datetime NOT NULL DEFAULT current_timestamp(),
  PRIMARY KEY (`id`),
  UNIQUE KEY `alias` (`alias`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Dumping data for table havana.groups_details: ~0 rows (approximately)
DELETE FROM `groups_details`;

-- Dumping structure for table havana.groups_edit_sessions
DROP TABLE IF EXISTS `groups_edit_sessions`;
CREATE TABLE IF NOT EXISTS `groups_edit_sessions` (
  `user_id` int(11) NOT NULL,
  `group_id` int(11) NOT NULL,
  `expire` bigint(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Dumping data for table havana.groups_edit_sessions: ~0 rows (approximately)
DELETE FROM `groups_edit_sessions`;

-- Dumping structure for table havana.groups_memberships
DROP TABLE IF EXISTS `groups_memberships`;
CREATE TABLE IF NOT EXISTS `groups_memberships` (
  `user_id` int(10) NOT NULL,
  `group_id` int(10) NOT NULL,
  `member_rank` enum('3','2','1') NOT NULL DEFAULT '1',
  `is_pending` tinyint(11) NOT NULL DEFAULT 0,
  `created_at` datetime NOT NULL DEFAULT current_timestamp(),
  KEY `userid` (`user_id`),
  KEY `groupid` (`group_id`),
  KEY `group_id` (`group_id`),
  KEY `user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Dumping data for table havana.groups_memberships: ~0 rows (approximately)
DELETE FROM `groups_memberships`;

-- Dumping structure for table havana.homes_details
DROP TABLE IF EXISTS `homes_details`;
CREATE TABLE IF NOT EXISTS `homes_details` (
  `user_id` int(10) NOT NULL,
  `background` varchar(255) NOT NULL DEFAULT 'bg_pattern_abstract2',
  UNIQUE KEY `user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Dumping data for table havana.homes_details: ~0 rows (approximately)
DELETE FROM `homes_details`;

-- Dumping structure for table havana.homes_edit_sessions
DROP TABLE IF EXISTS `homes_edit_sessions`;
CREATE TABLE IF NOT EXISTS `homes_edit_sessions` (
  `user_id` int(11) NOT NULL,
  `expire` bigint(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Dumping data for table havana.homes_edit_sessions: ~0 rows (approximately)
DELETE FROM `homes_edit_sessions`;

-- Dumping structure for table havana.homes_ratings
DROP TABLE IF EXISTS `homes_ratings`;
CREATE TABLE IF NOT EXISTS `homes_ratings` (
  `user_id` int(11) NOT NULL,
  `home_id` int(11) NOT NULL,
  `rating` int(11) NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Dumping data for table havana.homes_ratings: ~0 rows (approximately)
DELETE FROM `homes_ratings`;

-- Dumping structure for table havana.housekeeping_audit_log
DROP TABLE IF EXISTS `housekeeping_audit_log`;
CREATE TABLE IF NOT EXISTS `housekeeping_audit_log` (
  `action` enum('alert_user','kick_user','ban_user','room_alert','room_kick') NOT NULL,
  `user_id` int(11) NOT NULL,
  `target_id` int(11) NOT NULL DEFAULT -1,
  `message` varchar(255) NOT NULL DEFAULT '',
  `extra_notes` varchar(255) NOT NULL DEFAULT '',
  `created_at` datetime NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Dumping data for table havana.housekeeping_audit_log: ~0 rows (approximately)
DELETE FROM `housekeeping_audit_log`;

-- Dumping structure for table havana.infobus_polls
DROP TABLE IF EXISTS `infobus_polls`;
CREATE TABLE IF NOT EXISTS `infobus_polls` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `initiated_by` int(11) NOT NULL,
  `poll_data` text NOT NULL,
  `created_at` datetime NOT NULL DEFAULT current_timestamp(),
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Dumping data for table havana.infobus_polls: ~0 rows (approximately)
DELETE FROM `infobus_polls`;

-- Dumping structure for table havana.infobus_polls_answers
DROP TABLE IF EXISTS `infobus_polls_answers`;
CREATE TABLE IF NOT EXISTS `infobus_polls_answers` (
  `poll_id` int(11) NOT NULL,
  `answer` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  KEY `poll_id` (`poll_id`),
  KEY `user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Dumping data for table havana.infobus_polls_answers: ~0 rows (approximately)
DELETE FROM `infobus_polls_answers`;

-- Dumping structure for table havana.users_referred
DROP TABLE IF EXISTS `users_referred`;
CREATE TABLE IF NOT EXISTS `users_referred` (
  `user_id` int(11) DEFAULT NULL,
  `referred_id` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Dumping data for table havana.users_referred: ~0 rows (approximately)
DELETE FROM `users_referred`;

-- Dumping structure for table havana.users_tags
DROP TABLE IF EXISTS `users_tags`;
CREATE TABLE IF NOT EXISTS `users_tags` (
  `user_id` int(11) DEFAULT NULL,
  `tag` varchar(20) NOT NULL,
  `room_id` varchar(20) NOT NULL DEFAULT '0',
  `group_id` varchar(20) NOT NULL DEFAULT '0',
  `created_at` datetime NOT NULL DEFAULT current_timestamp(),
  KEY `user_id` (`user_id`),
  KEY `room_id` (`room_id`),
  KEY `group_id` (`group_id`),
  KEY `tag` (`tag`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Dumping data for table havana.users_tags: ~2 rows (approximately)
DELETE FROM `users_tags`;

ALTER TABLE `users_badges`
	CHANGE COLUMN `badge` `badge` CHAR(50) NOT NULL COLLATE 'utf8mb4_general_ci' AFTER `user_id`,
	ADD COLUMN `equipped` TINYINT(1) NOT NULL DEFAULT 0 AFTER `badge`,
	ADD COLUMN `slot_id` INT(11) NOT NULL DEFAULT 0 AFTER `equipped`;

DROP TABLE IF EXISTS `room_visits`;
CREATE TABLE IF NOT EXISTS `room_visits` (
  `room_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL DEFAULT 0,
  `visited_at` datetime NOT NULL DEFAULT current_timestamp(),
  UNIQUE KEY `room_id_user_id` (`room_id`,`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

package com.gsclannotifcations;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("main")
public interface GSClanNotificationsConfig extends Config
{

	@ConfigItem(
			keyName = "token",
			name = "Access Token",
			description = ""
	)
	default String token()
	{
		return null;
	}

	@ConfigItem(
			keyName = "bossdrop",
			name = "Boss Drops",
			description = "If selected will send boss drop notifications."
	)
	default boolean bossDrop()
	{
		return true;
	}

	@ConfigItem(
			keyName = "cofferdeposit",
			name = "Coffer Deposits",
			description = "If selected will send coffer deposit notifications."
	)
	default boolean cofferDeposit()
	{
		return true;
	}

	@ConfigItem(
			keyName = "cofferwithdraw",
			name = "Coffer Withdraws",
			description = "If selected will send coffer withdraw notifications."
	)
	default boolean cofferWithdraw()
	{
		return true;
	}

	@ConfigItem(
			keyName = "collectionlog",
			name = "Collection Log",
			description = "If selected will send collection log notifications."
	)
	default boolean collectionLog()
	{
		return true;
	}

	@ConfigItem(
			keyName = "combatAchievement",
			name = "Combat Achievements",
			description = "If selected will send combat achievement notifications."
	)
	default boolean combatAchievement()
	{
		return true;
	}

	@ConfigItem(
			keyName = "diary",
			name = "Achievement Diaries",
			description = "If selected will send achievement diary notifications."
	)
	default boolean diary()
	{
		return true;
	}

	@ConfigItem(
			keyName = "hardcore",
			name = "Hardcore Deaths",
			description = "If selected will send hardcore death notifications."
	)
	default boolean hardcore()
	{
		return true;
	}

	@ConfigItem(
			keyName = "levelup",
			name = "Level Up",
			description = "If selected will send level up notifications."
	)
	default boolean levelUp()
	{
		return true;
	}

	@ConfigItem(
			keyName = "pb",
			name = "Personal Best",
			description = "If selected will send personal best notifications."
	)
	default boolean pb()
	{
		return true;
	}
}

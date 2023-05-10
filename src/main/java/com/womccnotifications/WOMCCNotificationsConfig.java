package com.womccnotifications;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("example")
public interface WOMCCNotificationsConfig extends Config
{

	@ConfigItem(
			keyName = "womgroupid",
			name = "WOM Group ID",
			description = "wiseoldman.net group id"
	)
	default int womgroupid()
	{
		return 0;
	}

	@ConfigItem(
			keyName = "general",
			name = "Send General Broadcasts",
			description = "If selected will send general clan broadcasts."
	)
	default boolean general()
	{
		return true;
	}

	@ConfigItem(
			keyName = "skilling",
			name = "Send Skilling Broadcasts",
			description = "If selected will send skilling related clan broadcasts."
	)
	default boolean skilling()
	{
		return true;
	}

	@ConfigItem(
			keyName = "pvm",
			name = "Send PvM Broadcasts",
			description = "If selected will send PvM related broadcasts."
	)
	default boolean pvm()
	{
		return true;
	}

	@ConfigItem(
			keyName = "pvp",
			name = "Send PvP Broadcasts",
			description = "If selected will PvP related broadcasts."
	)
	default boolean pvp()
	{
		return true;
	}
}

package com.gsclannotifcations;

import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class GSClanNotificationsPluginTest
{
	public static void main(String[] args) throws Exception
	{
		ExternalPluginManager.loadBuiltin(GSClanNotificationsPlugin.class);
		RuneLite.main(args);
	}
}
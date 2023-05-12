package com.osrsclannotifcations;

import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class OSRSClanNotificationsPluginTest
{
	public static void main(String[] args) throws Exception
	{
		ExternalPluginManager.loadBuiltin(OSRSClanNotificationsPlugin.class);
		RuneLite.main(args);
	}
}
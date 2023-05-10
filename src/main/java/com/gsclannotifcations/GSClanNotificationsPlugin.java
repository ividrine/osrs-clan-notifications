package com.gsclannotifcations;

import javax.inject.Inject;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.inject.Provides;
import com.gsclannotifcations.notifications.Notification;
import com.gsclannotifcations.notifications.NotificationType;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.events.ChatMessage;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import okhttp3.*;

import java.io.IOException;
import java.text.DateFormat;
import java.util.Map;

import static java.util.Map.entry;

@Slf4j
@PluginDescriptor(
	name = "Glory Seekers(t) Notifications"
)
public class GSClanNotificationsPlugin extends Plugin
{
	@Inject
	private Client client;

	@Inject
	private GSClanNotificationsConfig config;

	@Inject
	private OkHttpClient httpClient;

	private final String BaseUrl = "localhost:7000";

	private final Gson gson = new GsonBuilder()
			.setDateFormat(DateFormat.FULL, DateFormat.FULL)
			.create();

	private final Map<NotificationType, String> compareMap = Map.ofEntries
	(
		entry(NotificationType.BOSSDROP, "received special loot"),
		entry(NotificationType.COFFERDEPOSIT, "has deposited"),
		entry(NotificationType.COFFERWITHDRAW, "has withdrawn"),
		entry(NotificationType.COLLECTIONLOG, "collection log"),
		entry(NotificationType.COMBATACHIEVEMENT, "combat achievements"),
		entry(NotificationType.DIARYCOMPLETION, "diary"),
		entry(NotificationType.HARDCOREDEATH, "hardcore"),
		entry(NotificationType.LEVELUP, "level"),
		entry(NotificationType.PERSONALBEST, "personal best")
	);

	@Subscribe
	public void onChatMessage(ChatMessage event)
	{
		ChatMessageType type = event.getType();
		String eventName = event.getName();
		String playerName = client.getLocalPlayer().getName();

		// if the message is a clan notification, and it comes from the current player
		if (type == ChatMessageType.CLAN_MESSAGE && eventName == playerName) {

			if(config.token() == null || config.token().trim().isEmpty()) return;

			Notification n = new Notification(event.getMessage(), compareMap);

			if (n.type == null) return;
			if (n.type == NotificationType.BOSSDROP && !config.bossDrop()) return;
			if (n.type == NotificationType.COFFERDEPOSIT && !config.cofferDeposit()) return;
			if (n.type == NotificationType.COFFERWITHDRAW && !config.cofferWithdraw()) return;
			if (n.type == NotificationType.COLLECTIONLOG && !config.collectionLog()) return;
			if (n.type == NotificationType.COMBATACHIEVEMENT && !config.combatAchievement()) return;
			if (n.type == NotificationType.DIARYCOMPLETION && !config.diary()) return;
			if (n.type == NotificationType.HARDCOREDEATH && !config.hardcore()) return;
			if (n.type == NotificationType.LEVELUP && !config.levelUp()) return;
			if (n.type == NotificationType.PERSONALBEST && !config.pb()) return;

			RequestBody payload = RequestBody.create(
					MediaType.parse("application/json; charset=utf-8"),
					gson.toJson(n)
			);

			Request request = new Request.Builder()
				.header("Authorization", "Bearer " + config.token())
				.url(BaseUrl + "/api/notification")
				.post(payload)
				.build();
			httpClient.newCall(request).enqueue(new Callback() {
				@Override
				public void onFailure(Call call, IOException e) {
					log.debug("Error", e);
				}

				@Override
				public void onResponse(Call call, Response response) throws IOException {
					response.close();
				}
			});
		}
	}

	@Provides
	GSClanNotificationsConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(GSClanNotificationsConfig.class);
	}

}

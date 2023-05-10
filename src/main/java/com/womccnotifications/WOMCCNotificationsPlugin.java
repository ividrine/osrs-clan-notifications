package com.womccnotifications;

import com.google.inject.Provides;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.events.ChatMessage;
import net.runelite.api.events.GameStateChanged;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import okhttp3.*;

import java.io.IOException;
import java.net.http.HttpRequest;

@Slf4j
@PluginDescriptor(
	name = "Wise Old Man CC Notifications"
)
public class WOMCCNotificationsPlugin extends Plugin
{
	@Inject
	private Client client;

	@Inject
	private WOMCCNotificationsConfig config;

	@Inject
	private OkHttpClient httpClient;

	private String BaseUrl = "localhost:7000";

	@Override
	protected void startUp() throws Exception
	{
		log.info("Example started!");
	}

	@Override
	protected void shutDown() throws Exception
	{
		log.info("Example stopped!");
	}

	@Subscribe
	public void onChatMessage(ChatMessage event)
	{
		ChatMessageType type = event.getType();
		String eventName = event.getName();
		String playerName = client.getLocalPlayer().getName();

		if (type == ChatMessageType.CLAN_MESSAGE && eventName == playerName) {

			Notification n = new Notification(event.getMessage());

			if (n.type == NotificationType.NONE) return;
			if (n.type == NotificationType.GENERAL && !config.general()) return;
			if (n.type == NotificationType.SKILLING && !config.skilling()) return;
			if (n.type == NotificationType.PVM && !config.pvm()) return;
			if (n.type == NotificationType.PVP && !config.pvp()) return;

			Request request = new Request.Builder()
				.url(BaseUrl + "/event")
				.post()
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
	@Subscribe
	public void onGameStateChanged(GameStateChanged gameStateChanged)
	{
//		if (gameStateChanged.getGameState() == GameState.LOGGED_IN)
//		{
//			client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", "Example says " + config.greeting(), null);
//		}
	}

	@Provides
	WOMCCNotificationsConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(WOMCCNotificationsConfig.class);
	}
}

package com.osrsclannotifcations;
import javax.imageio.ImageIO;
import javax.inject.Inject;
import com.google.inject.Provides;
import com.osrsclannotifcations.notifications.Notification;
import com.osrsclannotifcations.notifications.NotificationType;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.events.ChatMessage;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.DrawManager;
import net.runelite.client.util.ImageCapture;
import okhttp3.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Map.entry;

@Slf4j
@PluginDescriptor(
	name = "Glory Seekers (t)"
)
public class OSRSClanNotificationsPlugin extends Plugin
{
	@Inject
	private Client client;

	@Inject
	private OSRSClanNotificationsConfig config;

	@Inject
	private OkHttpClient httpClient;

	@Inject
	private ImageCapture imageCapture;

	@Inject
	private DrawManager drawManager;

	private final String BaseUrl = "https://osrs-clan-bot.herokuapp.com";


	Map<NotificationType, String> nLookup = new HashMap<NotificationType, String>()
	{
		{
			put(NotificationType.BOSSDROP, "received a drop");
			put(NotificationType.RAIDLOOT, "received special loot");
			put(NotificationType.CLUEITEM, "received a clue item");
			put(NotificationType.COFFERDEPOSIT, "has deposited");
			put(NotificationType.COFFERWITHDRAW, "has withdrawn");
			put(NotificationType.COLLECTIONLOG, "collection log");
			put(NotificationType.COMBATACHIEVEMENT, "combat achievements");
			put(NotificationType.DIARYCOMPLETION, "diary");
			put(NotificationType.HARDCOREDEATH, "hardcore");
			put(NotificationType.LEVELUP, "level");
		}
	};

	private static List<NotificationType> typesToScreenshot = Arrays.asList
	(
		NotificationType.BOSSDROP,
		NotificationType.RAIDLOOT,
		NotificationType.COLLECTIONLOG,
		NotificationType.CLUEITEM,
		NotificationType.DIARYCOMPLETION,
		NotificationType.LEVELUP,
		NotificationType.COMBATACHIEVEMENT
	);

	@Provides
	OSRSClanNotificationsConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(OSRSClanNotificationsConfig.class);
	}

	@Subscribe
	public void onChatMessage(ChatMessage event)
	{
		ChatMessageType type = event.getType();
		String playerName = client.getLocalPlayer().getName();
		String message = event.getMessage();

		// if the message is a clan notification, and relates to the current player
		if (type == ChatMessageType.CLAN_MESSAGE && message.contains(playerName)) {

			if(config.token() == null || config.token().trim().isEmpty()) return;

			Notification n = new Notification(playerName, message, nLookup);

			if (n.type == null) return;
			if (n.type == NotificationType.BOSSDROP && !config.bossDrop()) return;
			if (n.type == NotificationType.RAIDLOOT && !config.raidLoot()) return;
			if (n.type == NotificationType.CLUEITEM && !config.clueItem()) return;
			if (n.type == NotificationType.COFFERDEPOSIT && !config.cofferDeposit()) return;
			if (n.type == NotificationType.COLLECTIONLOG && !config.collectionLog()) return;
			if (n.type == NotificationType.COMBATACHIEVEMENT && !config.combatAchievement()) return;
			if (n.type == NotificationType.DIARYCOMPLETION && !config.diary()) return;
			if (n.type == NotificationType.HARDCOREDEATH && !config.hardcore()) return;
			if (n.type == NotificationType.LEVELUP && !config.levelUp()) return;

			BuildRequestAndSend(n);

		}
	}

	private void BuildRequestAndSend(Notification n) {

		MultipartBody.Builder bodyBuilder = new MultipartBody.Builder()
			.setType(MultipartBody.FORM)
			.addFormDataPart("rsn", n.rsn)
			.addFormDataPart("type", n.type.toString())
			.addFormDataPart("message", n.message);

		if (config.sendScreenshot() && typesToScreenshot.contains(n.type))
		{
			drawManager.requestNextFrameListener(image ->
			{
				BufferedImage bufferedImage = (BufferedImage) image;
				byte[] imageBytes;
				try
				{
					imageBytes = convertImageToByteArray(bufferedImage);
				}
				catch (IOException e)
				{
					log.warn("Error converting the image to byte array", e);
					return;
				}

				imageCapture.takeScreenshot
				(
					bufferedImage, "screenshot",
					null,
					false,
					null
				);

				bodyBuilder.addFormDataPart
				(
					"screenshot",
					"screenshot.png",
					RequestBody.create(MediaType.parse("image/png"), imageBytes)
				);

				SendRequest(bodyBuilder);

			});
		}
		else
		{
			SendRequest(bodyBuilder);
		}

	}

	private void SendRequest(MultipartBody.Builder bodyBuilder)
	{
		RequestBody payload = bodyBuilder.build();

		Request request = new Request.Builder()
			.header("Authorization", "Bearer " + config.token())
			.url(BaseUrl + "/api/notifications")
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

	private static byte[] convertImageToByteArray(BufferedImage bufferedImage) throws IOException
	{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ImageIO.write(bufferedImage, "png", baos);
		baos.flush();
		byte[] imageBytes = baos.toByteArray();
		baos.close();
		return imageBytes;
	}

}

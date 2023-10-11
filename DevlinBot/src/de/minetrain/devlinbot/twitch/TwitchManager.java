package de.minetrain.devlinbot.twitch;

import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.TimeZone;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.philippheuer.credentialmanager.domain.OAuth2Credential;
import com.github.philippheuer.events4j.simple.SimpleEventHandler;
import com.github.twitch4j.TwitchClient;
import com.github.twitch4j.TwitchClientBuilder;
import com.github.twitch4j.chat.events.AbstractChannelMessageEvent;
import com.github.twitch4j.chat.events.channel.ChannelMessageEvent;

import de.minetrain.devlinbot.config.obj.TwitchCredentials;
import de.minetrain.devlinbot.main.Main;
import io.github.bucket4j.Bandwidth;

/**
 * The TwitchManager class is responsible for creating and managing a Twitch client instance. 
 * It provides the functionality to join a Twitch chat and send messages to it.
 * 
 * @author MineTrain/Justin
 * @since 28.04.2023
 * @version 1.3
 */
public class TwitchManager {
	private static final Logger logger = LoggerFactory.getLogger(TwitchManager.class);
	public static TwitchClient twitch; //The static TwitchClient instance for managing Twitch interactions.
	
	/**
	 * Creates a new Twitch client instance using the provided TwitchCredentials.
	 * @param credentials The TwitchCredentials used to authenticate the Twitch client.
	 */
	public TwitchManager(TwitchCredentials credentials) {
		TwitchClientBuilder twitchBuilder = TwitchClientBuilder.builder();
		
		//Configure the TwitchClientBuilder with the provided credentials.
		twitch=twitchBuilder
			.withClientId(credentials.getClientId())
			.withClientSecret(credentials.getClientSecret())
			.withEnableHelix(true)
			.withChatAccount(new OAuth2Credential("twitch", credentials.getUserOauth2Token()))
	        .withEnableChat(true)
	        .withChatChannelMessageLimit(Bandwidth.simple(1, Duration.ofSeconds(1)).withId("per-channel-limit")) //Set the message raid limit for normal users.
			.build();
		
		
		twitch.getChat().joinChannel(Main.SETTINGS.getReplyChannelName()); //Join the channel specified in the settings file.
		twitch.getEventManager().getEventHandler(SimpleEventHandler.class).registerListener(new TwitchListner()); //Register a listener for Twitch events.
		logger.info("Connecting to channels: "+twitch.getChat().getChannels().toString()); //Print all the connected channels
	}
	
	/**
	 * Sends a message to the specified Twitch chat.
	 * 
	 * @param channel The name of the Twitch channel to send the message to.
	 * @param user The name of the user sending the message.
	 * @param message The message to be sent to the Twitch chat channel.
	 */
	public static void sendMessage(String channel, String user, String message) {
		logger.debug("Sending message -> message"); //Log the sent message.
		
		//Send the message to the specified Twitch chat.
		twitch.getChat().sendMessage(channel, formatMessage(channel, user, message));
		
	}

	private static String formatMessage(String channel, String user, String message) {
		TimeZone.setDefault(TimeZone.getTimeZone("Europe/Berlin")); //Set the default time zone.
		message = message 
			.replace("{USER}", (user == null) ? "" : "@"+user)
			.replace("{STREAMER}", (channel == null) ? "" : "@"+channel)
			.replace("{TIME}", LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm", Locale.GERMAN)))
			.replace("{UP}", Main.SETTINGS.getStreamUpTranslation())
			.replace("{DOWN}", Main.SETTINGS.getStreamDownTranslation());
		return message;
	}
	
	/**
	 * Sends a message to the specified Twitch chat channel using the information from the provided {@link ChannelMessageEvent}.
	 *
	 * @param event The {@link AbstractChannelMessageEvent} containing information about the chat channel and user.
	 * @param message The message to be sent to the Twitch chat channel.
	 */
	public static void sendMessage(AbstractChannelMessageEvent event, String message) {
		twitch.getChat().sendMessage(event.getChannel().getName()
				, formatMessage(event.getChannel().getName(), event.getUser().getName(), message)
				, event.getMessageEvent().getNonce().get()
				, event.getMessageEvent().getMessageId().get());
	}

}

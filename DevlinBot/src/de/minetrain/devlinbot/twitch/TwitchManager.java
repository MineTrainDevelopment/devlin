package de.minetrain.devlinbot.twitch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.philippheuer.credentialmanager.domain.OAuth2Credential;
import com.github.philippheuer.events4j.simple.SimpleEventHandler;
import com.github.twitch4j.TwitchClient;
import com.github.twitch4j.TwitchClientBuilder;

import de.minetrain.devlinbot.config.obj.TwitchCredentials;
import de.minetrain.devlinbot.main.Main;

/**
 * @author MineTrain/Justin
 * @since 26.04.2023
 * @version 1.0
 */
public class TwitchManager {
	private static final Logger logger = LoggerFactory.getLogger(TwitchManager.class);
	public static TwitchClient twitch;
	
	public TwitchManager(TwitchCredentials credentials) {
		TwitchClientBuilder twitchBuilder = TwitchClientBuilder.builder();
		
		twitch=twitchBuilder
			.withClientId(credentials.getClientId())
			.withClientSecret(credentials.getClientSecret())
			.withEnableHelix(true)
			.withChatAccount(new OAuth2Credential("twitch", credentials.getUserOauth2Token()))
	        .withEnableChat(true)
			.build();
		
		twitch.getChat().joinChannel(Main.SETTINGS.getReplyChannelName());
		twitch.getEventManager().getEventHandler(SimpleEventHandler.class).registerListener(new TwitchListner());
		logger.info("Connecting to channels: "+twitch.getChat().getChannels().toString());
	}
}

package de.minetrain.devlinbot.twitch;

import com.github.philippheuer.credentialmanager.domain.OAuth2Credential;
import com.github.philippheuer.events4j.simple.SimpleEventHandler;
import com.github.twitch4j.TwitchClient;
import com.github.twitch4j.TwitchClientBuilder;

/**
 * @author MineTrain/Justin
 * @since 26.04.2023
 * @version 1.0
 */
public class TwitchManager {
	public static TwitchClient twitch;
	
	public TwitchManager(String clientId, String clientSecret, String oAuth2Credential, String channelName) {
		TwitchClientBuilder twitchBuilder = TwitchClientBuilder.builder();
		
		twitch=twitchBuilder
			.withClientId(clientId)
			.withClientSecret(clientSecret)
			.withEnableHelix(true)
			.withChatAccount(new OAuth2Credential("twitch", oAuth2Credential))
	        .withEnableChat(true)
			.build();
		
		twitch.getChat().joinChannel(channelName);
		twitch.getEventManager().getEventHandler(SimpleEventHandler.class).registerListener(new TwitchListner());
		System.out.println("Connecting to channels: "+twitch.getChat().getChannels().toString());
	}
}

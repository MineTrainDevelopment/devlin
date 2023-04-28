package de.minetrain.devlinbot.config.obj;

import de.minetrain.devlinbot.config.ConfigManager;

/**
 * This class represents Twitch credentials, which include a client ID, client secret,
 * and user OAuth2 token.
 * 
 * @author MineTrain/Justin
 * @since 28.04.2023
 * @version 1.2
 */
public class TwitchCredentials {
	private final String clientId;
	private final String clientSecret;
	private final String userOauth2Token;
	
	/**
	 * Constructor for the TwitchCredentials class.
	 * @param config The current instance of the config.
	 */
	public TwitchCredentials(ConfigManager config) {
		this.clientId = config.getString("TwitchAPI.ClientID");
		this.clientSecret = config.getString("TwitchAPI.CLientSecret");
		this.userOauth2Token = config.getString("TwitchChannel.ChatAccountToken");
	}
	
	/**
	 * Constructor for the TwitchCredentials class.
	 * @param clientId The client ID for the Twitch application.
	 * @param clientSecret The client secret for the Twitch application.
	 * @param userOauth2Token The OAuth2 token for the user.
	 */
	public TwitchCredentials(String clientId, String clientSecret, String userOauth2Token) {
		// Initialize the fields with the provided values
		this.clientId = clientId;
		this.clientSecret = clientSecret;
		this.userOauth2Token = userOauth2Token;
	}

	/**
	 * @return The client ID for the Twitch application.
	 */
	public String getClientId() {
		return clientId;
	}

	/**
	 * @return The client secret for the Twitch application.
	 */
	public String getClientSecret() {
		return clientSecret;
	}

	/**
	 * @return The OAuth2 token for the chat user.
	 */
	public String getUserOauth2Token() {
		return userOauth2Token;
	}
	
	
}

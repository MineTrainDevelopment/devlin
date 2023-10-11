package de.minetrain.devlinbot.config.obj;

import de.minetrain.devlinbot.config.ConfigManager;

/**
 * This class represents settings for a Twitch bot, including a trigger word, reply delay,
 * reply channel name, and translations for when a stream goes up or down.
 *
 * @author MineTrain/Justin
 * @since 28.24.2023
 * @version 1.2
 */
public class Settings {
	private final Long replyDelay;
	private final String replyChannelName;
	private final String streamDownTranslation;
	private final String streamUpTranslation;
	private final Long requestBasedResponseChance;
	private final JokeSystemSettings jokeSystemSettings;
	
	
	/**
	 * Constructor for the Settings class.
	 * @param config The current instance of the config.
	 */
	public Settings(ConfigManager config) {
		this.replyDelay = config.getLong("Settings.ReplyDelay", 60);
		this.replyChannelName = config.getString("TwitchChannel.ReplyChannel");
		this.streamDownTranslation = config.getString("Settings.StreamDownTranslation", "Stream has stopt!");
		this.streamUpTranslation = config.getString("Settings.StreamUpTranslation", "Stream has startet!");
		this.requestBasedResponseChance = config.getLong("Settings.RequestBasedResponseChance", 20);
		this.jokeSystemSettings = new JokeSystemSettings(config);
	}

	/**
	 * @return The delay between bot replies.
	 */
	public Long getReplyDelay() {
		return replyDelay;
	}

	/**
	 * @return The name of the channel to send bot replies in.
	 */
	public String getReplyChannelName() {
		return replyChannelName;
	}

	/**
	 * @return The translation for when a stream goes down.
	 */
	public String getStreamDownTranslation() {
		return streamDownTranslation;
	}

	/**
	 * @return The translation for when a stream goes up.
	 */
	public String getStreamUpTranslation() {
		return streamUpTranslation;
	}

	/**
	 * @return The translation for when a stream goes up.
	 */
	public Long getRequestBasedResponseChance() {
		return requestBasedResponseChance;
	}
	
	
	public JokeSystemSettings getJokeSystemSettings(){
		return jokeSystemSettings;
	}
	
	
}

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
	private final String triggerWord;
	private final Long replyDelay;
	private final String replyChannelName;
	private final String streamDownTranslation;
	private final String streamUpTranslation;
	
	/**
	 * Constructor for the Settings class.
	 * @param config The current instance of the config.
	 */
	public Settings(ConfigManager config) {
		this.triggerWord = config.getString("Settings.TriggerWord");
		this.replyDelay = config.getLong("Settings.ReplyDelay");
		this.replyChannelName = config.getString("TwitchChannel.ReplyChannel");
		this.streamDownTranslation = config.getString("Settings.StreamDownTranslation");
		this.streamUpTranslation = config.getString("Settings.StreamUpTranslation");
	}

	/**
	 * @return The trigger word for the bot.
	 */
	public String getTriggerWord() {
		return triggerWord;
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
	
}

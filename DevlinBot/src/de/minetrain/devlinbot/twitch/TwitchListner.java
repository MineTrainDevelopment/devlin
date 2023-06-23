package de.minetrain.devlinbot.twitch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.philippheuer.events4j.simple.domain.EventSubscriber;
import com.github.twitch4j.chat.events.AbstractChannelMessageEvent;
import com.github.twitch4j.chat.events.channel.ChannelMessageEvent;
import com.github.twitch4j.events.ChannelGoLiveEvent;
import com.github.twitch4j.events.ChannelGoOfflineEvent;

import de.minetrain.devlinbot.main.Main;
import de.minetrain.devlinbot.resources.Messages;
import de.minetrain.devlinbot.userinput.ChatCommandManager;

/**
 * A listener for Twitch events such as streams going live or offline and channel messages.
 * Provides methods to handle these events and execute commands.
 * 
 * @author MineTrain/Justin
 * @since 28.04.2023
 * @version 1.1
 */
public class TwitchListner {
	private static final Logger logger = LoggerFactory.getLogger(TwitchListner.class);
	private static ChatCommandManager COMMAND_MANAGER;
	private static Messages messages;
	public static Long lastCallTime;
//	public static Long lastStreamUpTime = System.currentTimeMillis() - 7200000;
	
	/**
	 * Constructs a new TwitchListener object and initializes the command manager and messages.
	 */
	public TwitchListner() {
		lastCallTime = System.currentTimeMillis() - Main.SETTINGS.getReplyDelay()*1000;
		COMMAND_MANAGER = new ChatCommandManager();
		messages = new Messages(Main.CONFIG);
	}
	
	/**
	 * Handles the event when a stream goes live and sends a message to the channel with a randomized stream-up sentence.
	 * @param event The {@link ChannelGoLiveEvent} object containing information about the stream.
	 */
	@EventSubscriber
	public void onStreamUp(ChannelGoLiveEvent event){
		logger.info("Twtich livestram startet: "+event.getStream().getUserName()+" | "+event.getStream().getViewerCount()+" | "+event.getStream().getTitle());
		TwitchManager.sendMessage(event.getChannel().getName(), null, messages.getRandomStreamUpSentences());
		
//		logger.debug("Cooldown: "+System.currentTimeMillis() +"-"+lastStreamUpTime);
		
//		if((System.currentTimeMillis() - lastStreamUpTime) > 7200*1000){
//			lastStreamUpTime = System.currentTimeMillis();
//		}
		
		
	}

	/**
	 * Handles the event when a stream goes offline and sends a message to the channel with a randomized stream-down sentence.
	 * @param event The {@link ChannelGoOfflineEvent} object containing information about the channel.
	 */
	@EventSubscriber
	public void onStreamDown(ChannelGoOfflineEvent event){
		logger.info("Twtich livestram Offline: "+event.getChannel().getName());
		TwitchManager.sendMessage(event.getChannel().getName(), null, messages.getRandomStreamUpSentences());
	}
	
	/**
	 * Handles the event when a message is sent in the channel and executes the command if the cooldown time has elapsed.
	 * @param event The {@link ChannelMessageEvent} object containing information about the message.
	 */
	@EventSubscriber
	public void onChannelMessage(AbstractChannelMessageEvent event){
		logger.info("User: "+event.getUser().getName()+" | Message --> "+event.getMessage());
		if(isCoolDown()){COMMAND_MANAGER.execute(event, messages);}
	}

	/**
	 * Checks if the cooldown time has elapsed since the last message was sent.
	 * @return true if the cooldown time has elapsed, false otherwise.
	 */
	private boolean isCoolDown() {
		logger.debug("Cooldown: "+System.currentTimeMillis() +"-"+lastCallTime);
		if((System.currentTimeMillis() - lastCallTime) > Main.SETTINGS.getReplyDelay()*1000){
			lastCallTime = System.currentTimeMillis();
			return true;
		}
		
		return false;
	}
	
}

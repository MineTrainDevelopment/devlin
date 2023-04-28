package de.minetrain.devlinbot.twitch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.philippheuer.events4j.simple.domain.EventSubscriber;
import com.github.twitch4j.chat.events.channel.ChannelMessageEvent;
import com.github.twitch4j.events.ChannelGoLiveEvent;
import com.github.twitch4j.events.ChannelGoOfflineEvent;

import de.minetrain.devlinbot.main.Main;
import de.minetrain.devlinbot.resources.Messages;
import de.minetrain.devlinbot.userinput.ChatCommandManager;

/**
 * 
 * @author MineTrain/Justin
 * @since 26.04.2023
 * @version 1.0
 */
public class TwitchListner {
	private static final Logger logger = LoggerFactory.getLogger(TwitchListner.class);
	private static ChatCommandManager COMMAND_MANAGER;
	private static Messages messages;
	public static Long lastCallTime;
//	public static Long lastStreamUpTime = System.currentTimeMillis() - 7200000;
	
	public TwitchListner() {
		lastCallTime = System.currentTimeMillis() - Main.SETTINGS.getReplyDelay()*1000;
		COMMAND_MANAGER = new ChatCommandManager();
		messages = new Messages(Main.CONFIG);
	}
	
	@EventSubscriber
	public void onStreamUp(ChannelGoLiveEvent event){
		logger.info("Twtich livestram startet: "+event.getStream().getUserName()+" | "+event.getStream().getViewerCount()+" | "+event.getStream().getTitle());
//		logger.debug("Cooldown: "+System.currentTimeMillis() +"-"+lastStreamUpTime);
		
//		if((System.currentTimeMillis() - lastStreamUpTime) > 7200*1000){
//			lastStreamUpTime = System.currentTimeMillis();
			TwitchManager.sendMessage(event.getChannel().getName(), null, messages.getRandomStreamUpSentences());
//		}
		
		
	}


	@EventSubscriber
	public void onStreamDown(ChannelGoOfflineEvent event){
		logger.info("Twtich livestram Offline: "+event.getChannel().getName());
		
		TwitchManager.twitch.getChat().sendMessage(event.getChannel().getName(), messages.getRandomStreamDownSentences()
			.replace("{STREAMER}", "@"+event.getChannel().getName())
			.replace("{TIME}", "TIME-NULL")
			.replace("{DOWN}", Main.SETTINGS.getStreamDownTranslation()));
	}
	
	
	@EventSubscriber
	public void onChannelMessage(ChannelMessageEvent event){
		logger.info("User: "+event.getUser().getName()+" | Message --> "+event.getMessage());
		
		if(isCoolDown()){
			COMMAND_MANAGER.execute(event, messages);
		}
	}


	private boolean isCoolDown() {
		logger.debug("Cooldown: "+System.currentTimeMillis() +"-"+lastCallTime);
		if((System.currentTimeMillis() - lastCallTime) > Main.SETTINGS.getReplyDelay()*1000){
			lastCallTime = System.currentTimeMillis();
			return true;
		}
		
		return false;
	}
	
}

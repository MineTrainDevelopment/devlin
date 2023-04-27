package de.minetrain.devlinbot.twitch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.philippheuer.events4j.simple.domain.EventSubscriber;
import com.github.twitch4j.chat.events.channel.ChannelMessageEvent;
import com.github.twitch4j.events.ChannelGoLiveEvent;
import com.github.twitch4j.events.ChannelGoOfflineEvent;

import de.minetrain.devlinbot.main.Main;
import de.minetrain.devlinbot.resources.Messages;

/**
 * 
 * @author MineTrain/Justin
 * @since 26.04.2023
 * @version 1.0
 */
public class TwitchListner {
	private static final Logger logger = LoggerFactory.getLogger(TwitchListner.class);
	Messages messages = new Messages();
	public static Long lastCallTime = System.currentTimeMillis() - Main.messageDelay*1000;
	public static Long lastStreamUpTime = System.currentTimeMillis() - 7200000; 
	
	@EventSubscriber
	public void onStreamUp(ChannelGoLiveEvent event){
		logger.info("Twtich livestram startet: "+event.getStream().getUserName()+" | "+event.getStream().getViewerCount()+" | "+event.getStream().getTitle());
		logger.debug("Cooldown: "+System.currentTimeMillis() +"-"+lastStreamUpTime);
		
		if((System.currentTimeMillis() - lastStreamUpTime) > 7200*1000){
			lastStreamUpTime = System.currentTimeMillis();
			TwitchManager.twitch.getChat().sendMessage(event.getChannel().getName(), messages.getRandomStreamUpSentences()
				.replace("{STREAMER}", "@"+event.getChannel().getName())
				.replace("{TIME}", "TIME-NULL"));
		}
		
		
	}
	
	
	@EventSubscriber
	public void onStreamDown(ChannelGoOfflineEvent event){
		logger.info("Twtich livestram Offline: "+event.getChannel().getName());
		
		TwitchManager.twitch.getChat().sendMessage(event.getChannel().getName(), messages.getRandomStreamDownSentences()
			.replace("{STREAMER}", "@"+event.getChannel().getName())
			.replace("{TIME}", "TIME-NULL"));
	}
	
	
	@EventSubscriber
	public void onChannelMessage(ChannelMessageEvent event){
		logger.info("User: "+event.getUser().getName()+" | Message --> "+event.getMessage());
		logger.debug("Cooldown: "+System.currentTimeMillis() +"-"+lastCallTime);
		
		if((System.currentTimeMillis() - lastCallTime) > Main.messageDelay*1000){
			if(event.getMessage().toLowerCase().contains(Main.triggerWord.toLowerCase())){
				lastCallTime = System.currentTimeMillis();
				event.getTwitchChat().sendMessage(event.getChannel().getName(), messages.getRandomSillySentences()
					.replace("{USER}", "@"+event.getUser().getName())
					.replace("{STREAMER}", "@"+event.getChannel().getName())
					.replace("{TIME}", "TIME-NULL"));
			};
		}
		
		
		
	}
	

}

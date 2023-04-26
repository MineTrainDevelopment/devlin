package de.minetrain.devlinbot.twitch;

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
	Messages messages = new Messages();
	public static Long lastCallTime = 0l;
	public static Long lastStreamUpTime = 0l; 
	
	@EventSubscriber
	public void onStreamUp(ChannelGoLiveEvent event){
		System.out.println("Twtich livestram startet: "+event.getStream().getUserName()+" | "+event.getStream().getViewerCount()+" | "+event.getStream().getTitle());
		
		if(!isLastStreamUpCooledDown(7200)){
			TwitchManager.twitch.getChat().sendMessage(event.getChannel().getName(), messages.getRandomStreamUpSentences()
				.replace("{STREAMER}", "@"+event.getChannel().getName())
				.replace("{TIME}", "TIME-NULL"));
		}
	}
	
	
	@EventSubscriber
	public void onStreamDown(ChannelGoOfflineEvent event){
		System.out.println("Twtich livestram Offline: "+event.getChannel().getName());
		
		TwitchManager.twitch.getChat().sendMessage(event.getChannel().getName(), messages.getRandomStreamDownSentences()
			.replace("{STREAMER}", "@"+event.getChannel().getName())
			.replace("{TIME}", "TIME-NULL"));
	}
	
	
	@EventSubscriber
	public void onChannelMessage(ChannelMessageEvent event){
		System.out.println("User: "+event.getUser().getName()+" | Message --> "+event.getMessage());
		
		if(isLastCallCooledDown(Main.messageDelay) && event.getMessage().toLowerCase().contains(Main.triggerWord.toLowerCase())){
			event.getTwitchChat().sendMessage(event.getChannel().getName(), messages.getRandomSillySentences()
					.replace("{USER}", "@"+event.getUser().getName())
					.replace("{STREAMER}", "@"+event.getChannel().getName())
					.replace("{TIME}", "TIME-NULL"));
		}
	}
	

	/**
	 * Check whether a timestamp is further in the past than the specified time in seconds.
	 * @param delaySeconds {@link Long}
	 * @return whether the timestamp has cooled down.
	 */
	public boolean isLastCallCooledDown(long delaySeconds) {
		long currentTime = System.currentTimeMillis();
		
	    if (lastCallTime == 0 || (currentTime - lastCallTime) > delaySeconds*1000) {
	        lastCallTime = currentTime;
	        return true;
	    }
	    
	    return false;
	}
	
	/**
	 * Check whether a timestamp is further in the past than the specified time in seconds.
	 * @param delaySeconds {@link Long}
	 * @return whether the timestamp has cooled down.
	 */
	public boolean isLastStreamUpCooledDown(long delaySeconds) {
		long currentTime = System.currentTimeMillis();
		
	    if (lastCallTime == 0 || (currentTime - lastCallTime) > delaySeconds*1000) {
	        lastCallTime = currentTime;
	        return true;
	    }
	    
	    return false;
	}

}

package de.minetrain.devlinbot.userinput.commands;

import com.github.twitch4j.chat.events.channel.ChannelMessageEvent;

import de.minetrain.devlinbot.resources.Messages;
import de.minetrain.devlinbot.twitch.TwitchManager;
import de.minetrain.devlinbot.userinput.ChatCommand;

public class CustomCommand implements ChatCommand{
	private final String message;
	
	public CustomCommand(String message) {
		this.message = message;
	}
	
	@Override
	public void executeCommand(ChannelMessageEvent event, Messages messages) {
		TwitchManager.sendMessage(event, message);
	}

}

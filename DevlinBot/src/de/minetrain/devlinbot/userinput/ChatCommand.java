package de.minetrain.devlinbot.userinput;

import com.github.twitch4j.chat.events.channel.ChannelMessageEvent;

import de.minetrain.devlinbot.resources.Messages;

public interface ChatCommand {
	public void executeCommand(ChannelMessageEvent event, Messages messages);
}

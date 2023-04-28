package de.minetrain.devlinbot.userinput.commands;

import com.github.twitch4j.chat.events.channel.ChannelMessageEvent;

import de.minetrain.devlinbot.resources.Messages;
import de.minetrain.devlinbot.twitch.TwitchManager;
import de.minetrain.devlinbot.userinput.ChatCommand;

public class TimeCommand implements ChatCommand{

	@Override
	public void executeCommand(ChannelMessageEvent event, Messages messages) {
		TwitchManager.sendMessage(event.getChannel().getName(), event.getUser().getName(), messages.getRandomTimeSentences());
	}

}

package de.minetrain.devlinbot.userinput.commands;

import com.github.twitch4j.chat.events.AbstractChannelMessageEvent;

import de.minetrain.devlinbot.resources.Messages;
import de.minetrain.devlinbot.twitch.TwitchManager;
import de.minetrain.devlinbot.userinput.ChatCommand;

/**
 * The DefaultTriggerWordCommand class implements the ChatCommand interface, representing a chat command
 * that responds with a random silly sentence when triggered by a specific word.
 * 
 * @author MineTrain/Justin
 * @since 29.04.2023
 * @version 1.0
 */
public class DefaultTriggerWordCommand implements ChatCommand {
	
	/**
	 * Executes this command by sending a random silly sentence to the chat channel.
	 *
	 * @param event the {@link AbstractChannelMessageEvent} object representing the chat message event that triggered this command
	 * @param messages the {@link Messages} object representing the list of available messages.
	 */
	@Override
	public void executeCommand(AbstractChannelMessageEvent event, Messages messages) {
		TwitchManager.sendMessage(event, messages.getRandomSillySentences(event.getMessage()));
	}
	
}

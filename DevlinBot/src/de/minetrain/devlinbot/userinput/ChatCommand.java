package de.minetrain.devlinbot.userinput;

import com.github.twitch4j.chat.events.AbstractChannelMessageEvent;
import com.github.twitch4j.chat.events.channel.ChannelMessageEvent;

import de.minetrain.devlinbot.resources.Messages;

/**
 * The ChatCommand interface defines a contract for implementing chat commands.
 * A chat command is an action that can be triggered by a user in the chat, such as sending a message or performing an action.
 * 
 * @author MineTrain/Justin
 * @since 29.04.2023
 * @version 1.0
 */
public interface ChatCommand {
	
	/**
	 * Executes the command when triggered by a user in the chat.
	 *
	 * @param event the {@link AbstractChannelMessageEvent} object representing the chat message event that triggered this command
	 * @param messages the {@link Messages} object representing the list of available messages.
	 */
	public void executeCommand(AbstractChannelMessageEvent event, Messages messages);
}

package de.minetrain.devlinbot.userinput.commands;

import com.github.twitch4j.chat.events.channel.ChannelMessageEvent;

import de.minetrain.devlinbot.resources.Messages;
import de.minetrain.devlinbot.twitch.TwitchManager;
import de.minetrain.devlinbot.userinput.ChatCommand;

/**
 * The CustomCommand class implements the {@link ChatCommand} interface, representing a custom chat command
 * that sends a pre-defined message when executed.
 * 
 * @author MineTrain/Justin
 * @see 29.04.2023
 * @version 1.0
 */
public class CustomCommand implements ChatCommand{
	private final String message; //The message to be sent when this custom command is executed.
	
	/**
	 * Constructs a new CustomCommand object with the given message.
	 * @param message the message to be sent when this custom command is executed
	 */
	public CustomCommand(String message) {
		this.message = message;
	}
	
	/**
	 * Executes this custom command by sending the pre-defined message to the chat channel.
	 *
	 * @param event the {@link ChannelMessageEvent} object representing the chat message event that triggered this command
	 * @param messages the {@link Messages} object representing the list of available messages.
	 */
	@Override
	public void executeCommand(ChannelMessageEvent event, Messages messages) {
		TwitchManager.sendMessage(event, message);
	}

}

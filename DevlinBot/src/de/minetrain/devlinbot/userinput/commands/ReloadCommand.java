package de.minetrain.devlinbot.userinput.commands;

import java.io.FileNotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.twitch4j.chat.events.AbstractChannelMessageEvent;

import de.minetrain.devlinbot.main.ConsoleReader;
import de.minetrain.devlinbot.resources.Messages;
import de.minetrain.devlinbot.twitch.TwitchManager;
import de.minetrain.devlinbot.userinput.ChatCommand;

/**
 * The TimeCommand class implements the ChatCommand interface, representing a chat command
 * that responds with a random sentence related to time when executed.
 * 
 * @author MineTrain/Justin
 * @sinces 29.04.2023
 * @version 1.0
 */
public class ReloadCommand implements ChatCommand{
	public static Long lastCallTime = 0l;

	/**
	 * Executes this command by sending a random sentence related to time to the chat channel.
	 *
	 * @param event the {@link AbstractChannelMessageEvent} object representing the chat message event that triggered this command
	 * @param messages the {@link Messages} object representing the list of available messages.
	 */
	@Override
	public void executeCommand(AbstractChannelMessageEvent event, Messages messages) {
		if(event.getMessage().length() < 20 || isCoolDown()){
			TwitchManager.sendMessage(event, "Reloaded faild! Insufficient permissions!");
			return;
		}
		
		
				
		try {
			ConsoleReader.reload();
			TwitchManager.sendMessage(event, "Reloaded successfully!");
		} catch (FileNotFoundException ex) {
			LoggerFactory.getLogger(ReloadCommand.class).error("Invalid config!", ex);
			TwitchManager.sendMessage(event, "Reloaded faild! Check log files!");
		}
	}
	
	/**
	 * Checks if the cooldown time has elapsed since the last message was sent.
	 * @return true if the cooldown time has elapsed, false otherwise.
	 */
	private boolean isCoolDown() {
		if((System.currentTimeMillis() - lastCallTime) > 300*1000){
			lastCallTime = System.currentTimeMillis();
			return false;
		}
		
		return true;
	}

}

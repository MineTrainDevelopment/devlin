package de.minetrain.devlinbot.userinput;

import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import com.github.twitch4j.chat.events.AbstractChannelMessageEvent;

import de.minetrain.devlinbot.main.Main;
import de.minetrain.devlinbot.resources.Messages;
import de.minetrain.devlinbot.twitch.TwitchManager;
import de.minetrain.devlinbot.userinput.commands.CustomCommand;
import de.minetrain.devlinbot.userinput.commands.DefaultTriggerWordCommand;
import de.minetrain.devlinbot.userinput.commands.TimeCommand;

/**
 * The ChatCommandManager class manages the execution of chat commands.
 * It loads predefined commands from configuration settings, as well as custom commands defined by users.
 * 
 * @author MineTrain/Justin
 * @since 29.04.2023
 * @version 1.0
 */
public class ChatCommandManager {
	//A ConcurrentHashMap that maps trigger words to chat commands.
	private static final ConcurrentHashMap<String, ChatCommand> commands = new ConcurrentHashMap<String, ChatCommand>();
	
	/**
	 * Constructs a new ChatCommandManager and loads predefined commands from configuration settings,
	 * as well as custom commands defined by users.
	 */
	public ChatCommandManager() {
		//Load all config commands
		loadCommands();

//		Add additional commands by mapping trigger words to command classes:
//		commands.put("TriggerWord", CommandClass);

//		commands.put("!blame", new BlameCounter());
//		commands.put("!game", new GameCounter());
//		commands.put("!salz", new SalzCounter());
	}

	/**
	 * Load all custom commands from config.
	 */
	public void loadCommands() {
		commands.clear();
		Main.CONFIG.getStringList("Settings.TimeCommandTriggers").forEach(trigger -> commands.put(trigger, new TimeCommand()));
		Main.CONFIG.getStringList("Settings.TriggerWords").forEach(trigger -> commands.put(trigger, new DefaultTriggerWordCommand()));
		
		Main.CONFIG.getStringList("CustomCommands").forEach(command -> {
			if(command.contains("%")){
				String commandPrefix = command.split("%")[0].trim().strip();
				commands.put(commandPrefix, new CustomCommand(command.replace(commandPrefix, "").replace("%", "").strip().trim()));
				System.out.println(commandPrefix);
			}
		});
		
		commands.entrySet().forEach(entry -> System.out.println(entry.getKey()));
	}
	
	/**
	 * Executes the chat command triggered by a user in the chat.
	 *
	 * @param event the {@link AbstractChannelMessageEvent} object representing the chat message event that triggered the command
	 * @param messages the {@link Messages} object representing the list of available messages.
	 * @return true if a command was executed, false otherwise
	 */
	public boolean execute(AbstractChannelMessageEvent event, Messages messages) {
		ChatCommand cmd = null;
		
		//Search for a matching trigger word and associated command
		for(Entry<String, ChatCommand> entry : commands.entrySet()){
			if(event.getMessage().toLowerCase().contains(entry.getKey().toLowerCase())) {
				if(cmd == null){
					cmd = commands.get(entry.getKey());
				}
			}
		}
		
		//Execute the command if one was found
		if(cmd != null){
			cmd.executeCommand(event, messages);
			return true;
		}
		
		return false;
	}
}

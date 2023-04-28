package de.minetrain.devlinbot.userinput;

import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import com.github.twitch4j.chat.events.channel.ChannelMessageEvent;

import de.minetrain.devlinbot.main.Main;
import de.minetrain.devlinbot.resources.Messages;
import de.minetrain.devlinbot.userinput.commands.DefaultTriggerWordCommand;
import de.minetrain.devlinbot.userinput.commands.TimeCommand;

public class ChatCommandManager {
	private static final ConcurrentHashMap<String, ChatCommand> commands = new ConcurrentHashMap<String, ChatCommand>();
	
	public ChatCommandManager() {
		TimeCommand timeCommand = new TimeCommand();
		Main.CONFIG.getStringList("TimeCommandTriggers").forEach(trigger -> commands.put(trigger, timeCommand));

		commands.put(Main.SETTINGS.getTriggerWord(), new DefaultTriggerWordCommand());
//		commands.put("TriggerWord", CommandClass);
	}
	
	public boolean execute(ChannelMessageEvent event, Messages messages) {
		ChatCommand cmd = null;
		
		for(Entry<String, ChatCommand> entry : commands.entrySet()){
			if(event.getMessage().toLowerCase().contains(entry.getKey().toLowerCase())) {
				if(cmd == null){
					cmd = commands.get(entry.getKey());
				}
			}
		}
		
		if(cmd != null){
			cmd.executeCommand(event, messages);
			return true;
		}
		
		return false;
	}
}

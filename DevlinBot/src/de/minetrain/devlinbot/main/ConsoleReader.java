package de.minetrain.devlinbot.main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.minetrain.devlinbot.twitch.TwitchListner;
import de.minetrain.devlinbot.twitch.TwitchManager;

/**
 * The ConsoleReader class allows the program to read input from the console
 * and perform certain actions based on the user's input.
 * @author MineTrain/Justin
 * @since 29.04.2023
 * @version 1.0
 */
public class ConsoleReader {
	private static final Logger logger = LoggerFactory.getLogger(ConsoleReader.class);
	
	/**
	 * The default constructor for the ConsoleReader class. This creates a new
	 * thread that listens for console input and takes certain actions based on
	 * the user's input.
	 */
	public ConsoleReader() {
		new Thread(() -> { //Create a new thread that will listen for console input.
			try {
				String line = ""; //Initialize a variable to hold the user's input.
				BufferedReader reader = new BufferedReader(new InputStreamReader(System.in)); //Create a new BufferedReader object to read input from the console.
				while ((line = reader.readLine()) != null) { //Loop through the user's input, processing each line as it is entered.
					
					//Split the user's input into an array of strings, using a space as the delimiter. 
					//And perform different actions based on the user's input.
					switch (line.toLowerCase().split(" ")[0]) {
						//If the user entered "exit", log a warning message and exit the program.
						case "exit": 
							logger.warn("exit! - "+System.currentTimeMillis());
							System.exit(0);
							break;
							
						//If the user entered "say", send a message to a specified channel on Twitch.
						case "say": 
							System.out.println("Sending a message...");
							TwitchManager.sendMessage(Main.SETTINGS.getReplyChannelName(), null, "[CONSOLE] - "+line.replace("say ", ""));
							break;
						
						case "reload":
							System.out.println("Reload config...");
							Main.CONFIG.reloadConfig();
							TwitchListner.reloadMessages();
							TwitchListner.COMMAND_MANAGER.loadCommands();
							break;
						
						// If the user entered an unknown command, display a list of available commands.
						default:
							System.out.println("Commands:");
							System.out.println("   - exit (Shutdown the bot)");
							System.out.println("   - reload (Reload the config file)");
							System.out.println("   - say (send a message in the Twich chat)");
							break;
					}
				}
			} catch (IOException ex) {
				// If an error occurs while reading input from the console, log the error and continue running.
				logger.warn("Console reader throw an exception!", ex);
			}
		}).start();
	}
}

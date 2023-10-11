package de.minetrain.devlinbot.main;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.minetrain.devlinbot.config.ConfigManager;
import de.minetrain.devlinbot.config.obj.Settings;
import de.minetrain.devlinbot.config.obj.TwitchCredentials;
import de.minetrain.devlinbot.twitch.TwitchManager;
import de.minetrain.devlinbot.userinput.commands.WitzAPI;

/**
 * The Main class is responsible for initializing and starting the Twitch bot.
 * 
 * It contains the main method that starts the bot and reads the configuration
 * values from the "config.yml" file.
 * 
 * The main class can be started with command line arguments or fallback to the
 * configuration values.
 * 
 * The Twitch bot connects to the Twitch API using the provided client ID,
 * client secret, and OAuth2 credential.
 * 
 * @author MineTrain/Justin
 * @since 29.04.2023
 * @version 1.2
 */
public class Main {
	//Logger instance for logging messages
	private static final Logger logger = LoggerFactory.getLogger(Main.class);
	//Configuration manager instance to read configuration values from the "config.yml" file
	public static final ConfigManager CONFIG = new ConfigManager("config.yml"); 
	//Twitch credentials instance to store client ID, client secret, and OAuth2 credential
	private static TwitchCredentials CREDENTIALS;
	//Settings instance to store settings values
	public static Settings SETTINGS;
	
	/**
	 * The main method is responsible for starting the Twitch bot.
	 * 
	 * It reads the configuration values from the "config.yml" file and command line arguments
	 * to initialize the Twitch bot with custom credentials, if provided.
	 * 
	 * Finally, it initializes and starts the ConsoleReader to read user input from
	 * the console.
	 * 
	 * @param args the command line arguments to start the Twitch bot with custom credentials, if provided.
	 * @throws IOException if there is an error reading user input from the console.
	 */
	public static void main(String[] args) throws IOException {
		try {
			if(args.length > 3){
				//Initialize Twitch credentials using command line arguments
				logger.info("Starting with launch arguments!");
				CREDENTIALS = new TwitchCredentials(args[0], args[1], args[2]);
			}else{
				//Initialize Twitch credentials using configuration values
				logger.info("Invaild launch arguments! Fall back to config credentials!");
				CREDENTIALS = new TwitchCredentials(Main.CONFIG); 
			}
			
			//Fetch the client ID, client secret, and OAuth2 token from the Twitch credentials.
			String clientId = CREDENTIALS.getClientId();
			String clientSecret = CREDENTIALS.getClientSecret();
			String oAuth2Credential = CREDENTIALS.getUserOauth2Token();

			//Log initialization information
			logger.info(" ");
			logger.info("----------------------------------------------------------------");
			logger.info("Devlin bot by MineTrain startet...");
			logger.info("Should the bot not awnser you, you may neet to check the vaules:");
			logger.info(" ");
			logger.info("Client ID -> **********"+clientId.substring(clientId.length()-4, clientId.length()-1));
			logger.info("Client Secret -> **********"+clientSecret.substring(clientSecret.length()-4, clientSecret.length()-1));
			logger.info("OAuth2 -> **********"+oAuth2Credential.substring(oAuth2Credential.length()-4, oAuth2Credential.length()-1));
			logger.info("Channel Name -> "+SETTINGS.getReplyChannelName());
			logger.info("Triggers word -> "+Main.CONFIG.getStringList("Settings.TriggerWords").toString());
			logger.info("----------------------------------------------------------------");
			logger.info(" ");
			
			new TwitchManager(CREDENTIALS); // Initialize and start the Twitch bot
		} catch (Exception e) {
			//Log error and read user input from console
			logger.error((args.length >3) ? "You probably messed up your program arguments!" : "Somthing went wrong. You may open an GitHub issues and tell me about it!",e);
			System.in.read();
		}
		
		new ConsoleReader(); //Start the console reader, to respond to console inputs.
	}
	
}

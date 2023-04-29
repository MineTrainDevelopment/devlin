package de.minetrain.devlinbot.main;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.minetrain.devlinbot.config.ConfigManager;
import de.minetrain.devlinbot.config.obj.Settings;
import de.minetrain.devlinbot.config.obj.TwitchCredentials;
import de.minetrain.devlinbot.twitch.TwitchManager;

public class Main {
	private static final Logger logger = LoggerFactory.getLogger(Main.class);
	public static final ConfigManager CONFIG = new ConfigManager("config.yml");
	private static TwitchCredentials CREDENTIALS;
	public static Settings SETTINGS;
	
	public static void main(String[] args) throws IOException {
		try {
			SETTINGS = new Settings(CONFIG);
			
			if(args.length > 3){
				logger.info("Starting with launch arguments!");
				CREDENTIALS = new TwitchCredentials(args[0], args[1], args[2]);
			}else{
				logger.info("Invaild launch arguments! Fall back to config credentials!");
				CREDENTIALS = new TwitchCredentials(Main.CONFIG);
			}
			
			String clientId = CREDENTIALS.getClientId();
			String clientSecret = CREDENTIALS.getClientSecret();
			String oAuth2Credential = CREDENTIALS.getUserOauth2Token();

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
			
			new TwitchManager(CREDENTIALS);
		} catch (Exception e) {
			logger.error((args.length >3) ? "You probably messed up your program arguments!" : "Somthing went wrong. You may open an GitHub issues and tell me about it!",e);
			System.in.read();
		}
		
		new ConsoleReader();
	}
	
	
}

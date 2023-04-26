package de.minetrain.devlinbot.main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.minetrain.devlinbot.twitch.TwitchManager;

public class Main {
	private static final Logger logger = LoggerFactory.getLogger(Main.class);
	public static final Long messageDelay = 60l;
	public static final String triggerWord = "devlin";
	
	public static void main(String[] args) throws IOException {
		try {
			String clientId = args[0];
			String clientSecret = args[1];
			String oAuth2Credential = args[2];
			String channelName = args[3];
			
			logger.info("----------------------------------");
			logger.info("Devlin bot by MineTrain startet...");
			logger.info("Should the bot not awnser you, you may neet to check the vaules:");
			logger.info(" ");
			logger.info("Client ID -> **********"+clientId.substring(clientId.length()-4, clientId.length()-1));
			logger.info("Client Secret -> **********"+clientSecret.substring(clientSecret.length()-4, clientSecret.length()-1));
			logger.info("OAuth2 -> **********"+oAuth2Credential.substring(oAuth2Credential.length()-4, oAuth2Credential.length()-1));
			logger.info("Channel Name -> "+channelName);
			logger.info("----------------------------------");
			
			new TwitchManager(clientId, clientSecret, oAuth2Credential, channelName);
		} catch (Exception e) {
			logger.error("You probably messed up your program arguments!",e);
			System.in.read();
		}
		
		startInputReader();
	}
	
	//TODO add a config system.
	//TODO add a local time to TwitchListner

	
	public static void startInputReader() {
		new Thread(() -> {
			try {
				String line = "";
				BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
				while ((line = reader.readLine()) != null) {
					switch (line.toLowerCase()) {
					case "exit":
						logger.warn("exit! - "+System.currentTimeMillis());
						System.exit(0);
						break;
						
					default:
						break;
					}
				}
			} catch (IOException e) {
			}
		}).start();
	}
}

package de.minetrain.devlinbot.main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import de.minetrain.devlinbot.twitch.TwitchManager;

public class Main {
	public static final Long messageDelay = 60l;
	public static final String triggerWord = "devlin";
	
	public static void main(String[] args) throws IOException {
		try {
			String clientId = args[0];
			String clientSecret = args[1];
			String oAuth2Credential = args[2];
			String channelName = args[3];
			
			System.out.println("----------------------------------");
			System.out.println("Devlin bot by MineTrain startet...");
			System.out.println("Should the bot not awnser you, you may neet to check the vaules:");
			System.out.println(" ");
			System.out.println("Client ID -> **********"+clientId.substring(clientId.length()-4, clientId.length()-1));
			System.out.println("Client Secret -> **********"+clientSecret.substring(clientSecret.length()-4, clientSecret.length()-1));
			System.out.println("OAuth2 -> **********"+oAuth2Credential.substring(oAuth2Credential.length()-4, oAuth2Credential.length()-1));
			System.out.println("Channel Name -> "+channelName);
			System.out.println("----------------------------------");
			
			new TwitchManager(clientId, clientSecret, oAuth2Credential, channelName);
		} catch (Exception e) {
			System.err.println("You probably messed up your program arguments!");
			System.err.println(e);
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
						System.err.println("exit!");
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

package de.minetrain.devlinbot.main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.minetrain.devlinbot.twitch.TwitchManager;

public class ConsoleReader {
	private static final Logger logger = LoggerFactory.getLogger(ConsoleReader.class);
	
	public ConsoleReader() {
		new Thread(() -> {
			try {
				String line = "";
				BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
				while ((line = reader.readLine()) != null) {
					switch (line.toLowerCase().split(" ")[0]) {
					case "exit":
						logger.warn("exit! - "+System.currentTimeMillis());
						System.exit(0);
						break;
						
					case "say":
						TwitchManager.sendMessage(Main.SETTINGS.getReplyChannelName(), null, "[CONSOLE] - "+line.replace("say ", ""));
						break;
						
					default:
						System.out.println("Commands -> exit, say");
						break;
					}
				}
			} catch (IOException e) {
			}
		}).start();
	}
}

package de.minetrain.devlinbot.resources;

import java.util.List;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.minetrain.devlinbot.config.ConfigManager;

public class Messages {
	private static final Logger logger = LoggerFactory.getLogger(Messages.class);
	private final List<String> sillySentences;
	private final List<String> streamDownSentences;
	private final List<String> streamUpSentences;
	private final List<String> timeSentences;
	private final Random random = new Random();
	
	//{USER}
	//{STREAMER}
	//{TIME}
	public Messages(ConfigManager config) {
		logger.info("Inporting messages from config...");
		sillySentences = config.getStringList("SillySentences");
		streamDownSentences = config.getStringList("StreamDownSentences");
		streamUpSentences = config.getStringList("StreamUpSentences");
		timeSentences = config.getStringList("TimeSentences");
		logger.info("Messages imported successfully.");
	}
	
	
	public String getRandomSillySentences() {
		return sillySentences.get(random.nextInt(0, sillySentences.size()-1));
	}
	
	public String getRandomStreamDownSentences() {
		return streamDownSentences.get(random.nextInt(0, streamDownSentences.size()-1));
	}
	
	public String getRandomStreamUpSentences() {
		return streamUpSentences.get(random.nextInt(0, streamUpSentences.size()-1));
	}
	
	public String getRandomTimeSentences() {
		return timeSentences.get(random.nextInt(0, timeSentences.size()-1));
	}
}

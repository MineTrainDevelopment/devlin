package de.minetrain.devlinbot.resources;

import java.util.List;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.minetrain.devlinbot.config.ConfigManager;

/**
 * The Messages class represents a collection of different message types used in the application.
 * It contains lists of silly sentences, stream down sentences, stream up sentences, and time sentences.
 *  
 * These messages are imported from the application's configuration file using the ConfigManager.
 * The class provides methods to retrieve a random message from each of the different message types.
 * @author MineTrain/Justin
 * @since 29.04.2023
 * @version 1.0
 */
public class Messages {
	private static final Logger logger = LoggerFactory.getLogger(Messages.class);
	private final List<String> sillySentences;
	private final List<String> streamDownSentences;
	private final List<String> streamUpSentences;
	private final List<String> timeSentences;
	private final Random random = new Random();
	
	/**
	 * Constructs a Messages object by importing messages from the given ConfigManager.
	 * The messages are retrieved from the configuration file and stored in the appropriate
	 * lists for later use. 
	 *
	 * @param config the {@link ConfigManager} used to import messages
	 */
	public Messages(ConfigManager config) {
		logger.info("Inporting messages from config...");
		sillySentences = config.getStringList("SillySentences");
		streamDownSentences = config.getStringList("StreamDownSentences");
		streamUpSentences = config.getStringList("StreamUpSentences");
		timeSentences = config.getStringList("TimeSentences");
		logger.info("Messages imported successfully.");
	}
	
	/**
	 * @return a random silly sentence from the list of silly sentences.
	 */
	public String getRandomSillySentences() {
		return sillySentences.get(random.nextInt(0, sillySentences.size()-1));
	}
	
	/**
	 * @return a random stream down sentence from the list of stream down sentences.
	 */
	public String getRandomStreamDownSentences() {
		return streamDownSentences.get(random.nextInt(0, streamDownSentences.size()-1));
	}
	
	/**
	 * @return a random stream up sentence from the list of stream up sentences.
	 */
	public String getRandomStreamUpSentences() {
		return streamUpSentences.get(random.nextInt(0, streamUpSentences.size()-1));
	}
	
	/**
	 * @return a random time sentence from the list of time sentences.
	 */
	public String getRandomTimeSentences() {
		return timeSentences.get(random.nextInt(0, timeSentences.size()-1));
	}
}

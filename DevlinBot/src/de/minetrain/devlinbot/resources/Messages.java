package de.minetrain.devlinbot.resources;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.minetrain.devlinbot.config.ConfigManager;
import de.minetrain.devlinbot.main.Main;

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
	
	private final Map<String, List<String>> requestBasedResponses = new HashMap<String, List<String>>();
	
	/**
	 * Constructs a Messages object by importing messages from the given ConfigManager.
	 * The messages are retrieved from the configuration file and stored in the appropriate
	 * lists for later use. 
	 *
	 * @param config the {@link ConfigManager} used to import messages
	 */
	public Messages(ConfigManager config) {
		logger.info("Inporting messages from config...");
//		sillySentences = config.getStringList("SillySentences");
		streamDownSentences = config.getStringList("StreamDownSentences");
		streamUpSentences = config.getStringList("StreamUpSentences");
		timeSentences = config.getStringList("TimeSentences");
		List<String> newSentences = new ArrayList<>();
		
		config.getStringList("SillySentences").forEach(sentence -> {
			String keySplitter = "&{";
	        String pattern = "%\\{(.*?)\\}%";
	        String splitPattern = "&\\{(.*?)\\}&";
	        Matcher regexMatcher = Pattern.compile(pattern).matcher(sentence);

	        
			List<String> matchList = new ArrayList<String>();
			while (regexMatcher.find()) {// Finds Matching Pattern in String
				String key = regexMatcher.group(1).toLowerCase();
				matchList.add(key.replaceAll(splitPattern, "$1"));
				
				if(key.contains(keySplitter)){
			        Matcher splitMatcher = Pattern.compile(splitPattern).matcher(sentence);
			        while(splitMatcher.find()){
			        	matchList.add(splitMatcher.group(1));
			        }
				}
			}
			
			
			//replace the sentance with a formated one.
	        sentence = sentence.replaceAll(pattern, "$1").replaceAll(splitPattern, "$1");
	        newSentences.add(sentence);
			

			for(String key : matchList) {
				System.out.println(key+" - "+sentence+" - "+regexMatcher.groupCount());
				requestBasedResponses.computeIfAbsent(key, k -> new ArrayList<>()).add(sentence);
			}
	        
		});
		
		sillySentences = newSentences;
		logger.info("Messages imported successfully.");
	}
	
	/**
	 * @return A randomly selected response based on the input criteria or a random silly sentence if no matches are found.
	 */
	public String getRandomSillySentences(String input) {
		//Check if the method should return a random response based on the request-based response chance.
		if(!getRandomChance(Main.SETTINGS.getRequestBasedResponseChance())){
			return sillySentences.get(random.nextInt(0, sillySentences.size()));
		}
		
		//Create a regular expression pattern for matching against the input.
        Pattern pattern = Pattern.compile("^(" + String.join("|", input.replaceAll("[^a-zA-Z0-9\\s+]", "").split("\\s+")) + ")$");
        
		//Filter and collect matching responses based on the input.
		List<String> result = requestBasedResponses.entrySet().stream()
				   .filter(entry -> pattern.matcher(entry.getKey()).matches())
				   .flatMap(entry -> entry.getValue().stream()) // Combine lists into a single stream of strings
				   .collect(Collectors.toList());
		
		//If no matching responses are found, return a random silly sentence.
		if(result == null || result.isEmpty()){
			return sillySentences.get(random.nextInt(0, sillySentences.size()));
		}
		
		//Return a randomly selected response from the list of matching responses.
		return result.get(random.nextInt(0, result.size()));
	}
	

	
	/**
	 * @return a random stream down sentence from the list of stream down sentences.
	 */
	public String getRandomStreamDownSentences() {
		return streamDownSentences.get(random.nextInt(0, streamDownSentences.size()));
	}
	
	/**
	 * @return a random stream up sentence from the list of stream up sentences.
	 */
	public String getRandomStreamUpSentences() {
		return streamUpSentences.get(random.nextInt(0, streamUpSentences.size()));
	}
	
	/**
	 * @return a random time sentence from the list of time sentences.
	 */
	public String getRandomTimeSentences() {
		return timeSentences.get(random.nextInt(0, timeSentences.size()));
	}
	
	/**
	 * Calculates a random chance based on the provided probability.
	 *
	 * This method generates a random long value between 0 (inclusive) and 100 (exclusive)
	 * and checks if it is less than the given chance. If the random value is less than
	 * the chance, the method returns true; otherwise, it returns false.
	 *
	 * @param chance The probability in percentage (0 to 100) to calculate the chance.
	 * @return true if the random value is less than the chance, otherwise false.
	 */
	public boolean getRandomChance(long chance) {
        return random.nextLong(100) < chance;
    }
}

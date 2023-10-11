package de.minetrain.devlinbot.userinput.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.minetrain.devlinbot.config.obj.JokeSystemSettings;
import de.minetrain.devlinbot.resources.RandomArrayList;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;

public class WitzAPI {
	private static final Logger logger = LoggerFactory.getLogger(WitzAPI.class);
	private static final String userAgent = "DevlinBot (https://github.com/MineTrainDevelopment/devlin)";
	private final RandomArrayList<String> jokes = new RandomArrayList<String>();
	private final List<String> categorys;
	private final JokeSystemSettings settings;
	
	public WitzAPI(JokeSystemSettings settings) {
		this.settings = settings;
		logger.debug("Request a language check for witzapi.de!");
		HttpResponse<String> language = Unirest.get("https://witzapi.de/api/language/?abbreviation="+settings.getLanguage())
			  .header("Accept", "*/*")
			  .header("User-Agent", userAgent)
			  .asString();
		
		String languageBody = language.getBody().replaceAll("[\\[\\]{}]", "");
		if(languageBody.isEmpty() || !languageBody.contains(",")){
			logger.warn("Unsupported language code! -> '"+settings.getLanguage()+"' Try 'en' or 'de' instad!");
			this.categorys = new ArrayList<String>();
			return;
		}
		
		logger.debug("Request the avalible joke categorys");
		HttpResponse<String> categoryResponds = Unirest.get("https://witzapi.de/api/category/?language=" + settings.getLanguage())
				.header("Accept", "*/*")
				.header("User-Agent", userAgent)
				.asString();
		
		this.categorys = Arrays.asList(categoryResponds.getBody()
				.replaceAll("[\\[\\]{}]", "")
				.replace("\"name\":", "")
				.replace("\"language\":\"de\",", "")
				.replace("\"language\":\"de\"", "")
				.replace("\"", "")
				.split(","));
		
		
		if(categorys.isEmpty()){
			logger.warn("Your chosen language code has no joke categorys available! -> '"+settings.getLanguage()+"' Try 'en' or 'de' instad!");
			return;
		}
		
		collectNewJokes(100);
	}

	/**
	 * 
	 * @param amound A ruff estimation how many shoult be pullt.
	 */
	public void collectNewJokes(int amound) {
		int jokesPerCategory = amound / categorys.size();
		
		if(jokesPerCategory == 0){
			jokesPerCategory = 2;
		}
		
		logger.info("Requesting "+amound+" new Jokes from witze.de");
		for(String category : categorys){
			HttpResponse<String> jokeResponse = Unirest
				.get("https://witzapi.de/api/joke/?limit=" + jokesPerCategory + "&category=" + category + "&language=" + settings.getLanguage())
				.header("Accept", "*/*")
				.header("User-Agent", userAgent)
				.asString();
			
			List<String> jokes = Arrays.asList(jokeResponse.getBody()
				.replaceAll("[\\[\\]{}]", "")
				.replace("\\n", " ")
				.replace("\"language\":\"de\",", "")
				.replace("\"language\":\"de\"", "")
				.split("\"text\":\""));
			
			for(String joke : jokes){
				if(!joke.toLowerCase().contains("^(?:c[ds]u|afd|[fs]pd)$") && joke.length() > 2){ //Filter out stuff about German Political partys.
					this.jokes.add(joke.substring(0, joke.length()-2));
				}
			}
		};
	}
	
	public String getNewJoke(){
		logger.debug("Fetching a new joke sentence");
		if(jokes.size()<=1){
			collectNewJokes(100);
		}
		return jokes.getAndRemove();
	}

}

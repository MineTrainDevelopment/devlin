package de.minetrain.devlinbot.config.obj;

import java.util.List;

import de.minetrain.devlinbot.config.ConfigManager;

public class JokeSystemSettings {
	private final List<String> triggers;
	private boolean aktive;
	private final String language;
	private final boolean requireBotTriggerWord;
	
	public JokeSystemSettings(ConfigManager config) {
		this.triggers = config.getStringList("Settings.JokeCommand.Triggers");
		this.aktive = config.getBoolean("Settings.JokeCommand.isAktiv", false);
		this.language = config.getString("Settings.JokeCommand.JokeLanguage", "en");
		this.requireBotTriggerWord = config.getBoolean("Settings.JokeCommand.RequireBotTriggerWord", true);;
	}

	public List<String> getTriggers() {
		return triggers;
	}

	public boolean isAktive() {
		return aktive || !getTriggers().isEmpty();
	}

	public String getLanguage() {
		return language;
	}

	public boolean isRequireBotTriggerWord() {
		return requireBotTriggerWord;
	}
	
	public void setAktiv(boolean aktive){
		this.aktive = aktive;
	}
	
	
	
}

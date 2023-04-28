package de.minetrain.devlinbot.config;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.naming.ConfigurationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;

public class ConfigManager {
	private static final Logger logger = LoggerFactory.getLogger(ConfigManager.class);
	private final String configFileName;
    private final Yaml yaml;
    private Map<String, Object> config;

    public ConfigManager(String configFileName){
    	logger.info("Reading config file...");
    	this.configFileName = configFileName;
        yaml = new Yaml();
        
        try {
			reloadConfig();
		} catch (FileNotFoundException ex) {
			throw new IllegalArgumentException("Canot initialize ConfigManager. File not found!", ex);
		}
    }
    
    public final void reloadConfig() throws FileNotFoundException{
        config = yaml.load(new FileInputStream(configFileName));
    }
    
	public final boolean getBoolean(String path) {
		return getBoolean(path, false);
	}
    
	public final long getLong(String path) {
		return getLong(path, 0l);
	}
    
	public final int getInt(String path) {
		return getInt(path, 0);
	}
    
	public final String getString(String path) {
		return getString(path, ">null<");
	}
	
	public final String[] getStringArray(String path, String regex) {
        return getStringArray(path, regex, ">null<");
    }
	
	public final String[] getStringArray(String path, String regex, String defaultValue) {
        return getString(path, defaultValue).replace(" ", "").split(regex);
    }
	
	@SuppressWarnings("unchecked")
	public final boolean getBoolean(String path, boolean defaultValue) {
		String[] keys = path.split("\\.");
		Map<String, Object> current = config;
		for (String key : keys) {
			if (current.containsKey(key)) {
				Object value = current.get(key);
				if (value instanceof Map) {
					current = (Map<String, Object>) value;
				} else if (value instanceof Boolean) {
					return (boolean) value;
				}
			}
		}

		throwWarn(path);
		return defaultValue;
	}
	
	
	@SuppressWarnings("unchecked")
	public Long getLong(String path, long defaultValue) {
	    String[] keys = path.split("\\.");
	    Map<String, Object> current = config;
	    for (String key : keys) {
	        if (current.containsKey(key)) {
	            Object value = current.get(key);
	            if (value instanceof Map) {
	                current = (Map<String, Object>) value;
	            } else if (value instanceof Number) {
	                return ((Number) value).longValue();
	            }
	        }
	    }
	    
	    throwWarn(path);
	    return defaultValue;
	}
	
	/**
	 * 
	 * @param path
	 * @return
	 * @see ConfigManager#getLong(String)
	 */
	public int getInt(String path, int defaultValue) {
	    Long value = getLong(path, defaultValue);
	    if(value > Integer.MAX_VALUE){
	    	logger.warn("Intiger is to larg! Use getLong instat!");
	    	return defaultValue;
	    }
	    
	    return Integer.parseInt(String.valueOf(value));
	}


	@SuppressWarnings("unchecked")
	public final String getString(String path, String defaultValue) {
	    String[] keys = path.split("\\.");
	    Map<String, Object> current = config;
	    for (String key : keys) {
	        if (current.containsKey(key)) {
	            Object value = current.get(key);
	            if (value instanceof Map) {
	                current = (Map<String, Object>) value;
	            } else if (value instanceof String) {
	                return (String) value;
	            }
	        }
	    }
	    
	    throwWarn(path);
	    return defaultValue;
	}
	

	
	@SuppressWarnings("unchecked")
	public final List<Long> getLongList(String path) {
	    String[] keys = path.split("\\.");
	    Map<String, Object> current = config;
	    for (String key : keys) {
	        if (current.containsKey(key)) {
	            Object value = current.get(key);
	            if (value instanceof Map) {
	                current = (Map<String, Object>) value;
	            } else if (value instanceof List) {
	            	List<Object> values = (List<Object>) value;
	    	        List<Long> longList = new ArrayList<>();
	    	        
	    	        values.forEach(index -> {
	    	        	if(index instanceof Long || index instanceof Integer){
	    	                longList.add(Long.valueOf(String.valueOf(index)));
	    	        	}
	    	        });
	    	        
	    	        return longList;
	            }
	        }
	    }
	    

	    throwWarn(path);
	    return null;
	}
	

	@SuppressWarnings("unchecked")
	public final List<String> getStringList(String path) {
	    String[] keys = path.split("\\.");
	    Map<String, Object> current = config;
	    for (String key : keys) {
	        if (current.containsKey(key)) {
	            Object value = current.get(key);
	            if (value instanceof Map) {
	                current = (Map<String, Object>) value;
	            } else if (value instanceof List) {
	            	List<Object> values = (List<Object>) value;
	    	        List<String> stringList = new ArrayList<>();
	    	        
	    	        values.forEach(index -> {
	    	        	if(index instanceof String){
	    	        		stringList.add(String.valueOf(index));
	    	        	}
	    	        });
	    	        
	    	        return stringList;
	            }
	        }
	    }
	    

	    throwWarn(path);
	    return null;
	}
	

	private void throwWarn(String path) {
		logger.warn("Invalid config path!", new ConfigurationException("Cant find a value on this path: "+path));
	}
}

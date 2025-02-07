package nanoquiz.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public abstract /*final*/ class ConfigFileReader {

	public static Map<String, String> readConfigFile(File file) throws IOException, ConfigSyntaxException, ConfigDuplicateKeyException {
		try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
			Map<String, String> configData = new HashMap<>();
			String line;
			while((line = reader.readLine()) != null) {
				if(line.charAt(0) == '#') continue; // lines starting with '#' are comments
				if(line.length() == 0) continue;

				String[] data = line.split("=", 2);
				if (data.length != 2) {
					throw new ConfigSyntaxException();
				}

				String previous = configData.put(data[0].toLowerCase().strip(), data[1]);
				if(previous != null) {
					throw new ConfigDuplicateKeyException(data[0]);
				}
			}
			return configData;
		} catch(FileNotFoundException e) {
			return Map.of();
		}
	}

	public static abstract class ConfigParsingException extends Exception {
		public ConfigParsingException (String message) {
			super (message);
		}
	}
	
	public static final class ConfigSyntaxException extends ConfigParsingException {
		ConfigSyntaxException() {
			super("Invalid config file syntax.");
		}
	}

	public static final class ConfigDuplicateKeyException extends ConfigParsingException {
		ConfigDuplicateKeyException(String key) {
			super("Duplicate key in config file: " + key);
		}
	}
}

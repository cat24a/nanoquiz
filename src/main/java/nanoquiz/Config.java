package nanoquiz;

import java.io.IOException;
import java.util.Map;

import nanoquiz.util.ConfigFileReader;

public abstract /*final*/ class Config {
	public static boolean CHECK_FOR_UPDATES;

	public static float SCORE_DECAY;
	public static float SCORE_FACTOR;
	public static float CONFIDENCE;

	public static int GOOD_DELAY;
	public static int BAD_DELAY;
	public static int COOLDOWN;

	public static void loadConfig() throws IOException, ConfigFileReader.ConfigParsingException, NullPointerException {
		Map<String, String> configData = ConfigFileReader.readConfigFile(Main.workdir.resolve("config.txt").toFile());

		CHECK_FOR_UPDATES = Map.of("true", true, "false", false).get(configData.getOrDefault("check for updates", "true"));

		SCORE_DECAY = Float.parseFloat(configData.getOrDefault("score decay", "0.5"));
		SCORE_FACTOR = Float.parseFloat(configData.getOrDefault("score factor", "10000000"));
		CONFIDENCE = Float.parseFloat(configData.getOrDefault("confidence", "1"));

		GOOD_DELAY = Integer.parseInt(configData.getOrDefault("good delay", "1000"));
		BAD_DELAY = Integer.parseInt(configData.getOrDefault("bad delay", "5000"));
		COOLDOWN = Integer.parseInt(configData.getOrDefault("cooldown", "2000"));
	}
}

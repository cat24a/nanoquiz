package nanoquiz;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public abstract /*final*/ class Config {
	public static boolean CHECK_FOR_UPDATES;

	public static float SCORE_DECAY;
	public static float SCORE_FACTOR;
	public static float CONFIDENCE;

	public static int GOOD_DELAY;
	public static int BAD_DELAY;
	public static int COOLDOWN;

	public static Properties config;

	public static void loadConfig() throws IOException {
		config = new Properties();
		try (InputStream is = Config.class.getResourceAsStream("default-config")) {
			config.load(is);
		}
		try (InputStream is = new FileInputStream(Main.workdir.resolve("nq-config").toFile())) {
			Main.log.info(()->"Config file found — reading config from file.");
			config.load(is);
		} catch (FileNotFoundException e) {
			Main.log.info(()->"No config file found — using default options.");
		}

		CHECK_FOR_UPDATES = config.getProperty("no_update_check") == null;

		SCORE_DECAY = Float.parseFloat(config.getProperty("score_decay"));
		SCORE_FACTOR = Float.parseFloat(config.getProperty("score_factor"));
		CONFIDENCE = Float.parseFloat(config.getProperty("confidence"));

		GOOD_DELAY = Integer.parseInt(config.getProperty("delay_good"));
		BAD_DELAY = Integer.parseInt(config.getProperty("delay_bad"));
		COOLDOWN = Integer.parseInt(config.getProperty("cooldown"));
	}
}

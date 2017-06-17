package tdl.auth.test;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class TestConfiguration {

    private static final TestConfiguration INSTANCE;

    static {
        INSTANCE = new TestConfiguration();
    }

    private final Properties properties;

    private TestConfiguration() {
        properties = new Properties();
        try (InputStream stream = this.getClass().getResourceAsStream("/configuration.properties")) {
            properties.load(stream);
        } catch (IOException ex) {
            throw new RuntimeException("Cannot load configuration", ex);
        }
    }

    public static String getConfig(String key) {
        String config = INSTANCE.properties.getProperty(key);
        if (config == null) {
            throw new RuntimeException("Config [" + key + "] does not exists");
        }
        return config;
    }

}

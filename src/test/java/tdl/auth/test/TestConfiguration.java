package tdl.auth.test;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class TestConfiguration {

    private static final TestConfiguration INSTANCE;

    public static final String TEST_AWS_REGION;
    public static final String TEST_PUBLIC_PAGE_BUCKET;
    public static final String TEST_VIDEO_STORAGE_BUCKET;
    public static final String TEST_TDL_SCOPE;
    public static final String TEST_ROOT_USER_ACCESS_KEY_ID;
    public static final String TEST_ROOT_USER_SECRET_ACCESS_KEY;
    public static final String TEST_USER_ACCESS_KEY_ID;
    public static final String TEST_USER_SECRET_ACCESS_KEY;
    public static final String TEST_USERNAME;
    public static final String TEST_CHALLENGE;

    public static final String TEST_JWT_KEY_ARN;

    static {
        INSTANCE = new TestConfiguration();
        TEST_AWS_REGION = getConfig("TEST_AWS_REGION");
        TEST_PUBLIC_PAGE_BUCKET = getConfig("TEST_PUBLIC_PAGE_BUCKET");
        TEST_VIDEO_STORAGE_BUCKET = getConfig("TEST_VIDEO_STORAGE_BUCKET");
        TEST_TDL_SCOPE = getConfig("TEST_TDL_SCOPE");
        TEST_ROOT_USER_ACCESS_KEY_ID = getConfig("TEST_ROOT_USER_ACCESS_KEY_ID");
        TEST_ROOT_USER_SECRET_ACCESS_KEY = getConfig("TEST_ROOT_USER_SECRET_ACCESS_KEY");
        TEST_USER_ACCESS_KEY_ID = getConfig("TEST_USER_ACCESS_KEY_ID");
        TEST_USER_SECRET_ACCESS_KEY = getConfig("TEST_USER_SECRET_ACCESS_KEY");
        TEST_JWT_KEY_ARN = getConfig("TEST_JWT_KEY_ARN");
        TEST_CHALLENGE = getConfig("TEST_CHALLENGE");
        TEST_USERNAME = getConfig("TEST_USERNAME");

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

    private static String getConfig(String key) {
        String config = INSTANCE.properties.getProperty(key);
        if (config == null) {
            throw new RuntimeException("Config [" + key + "] does not exists");
        }
        return config;
    }

}

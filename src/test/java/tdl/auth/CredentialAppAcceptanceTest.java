package tdl.auth;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.EnvironmentVariables;
import static tdl.auth.test.TestConfiguration.getConfig;

public class CredentialAppAcceptanceTest {

    @Rule
    public final EnvironmentVariables environmentVariables = new EnvironmentVariables();

    @Before
    public void setUp() {
        environmentVariables.set("JWT_ENCRYPT_KEY_ARN", getConfig("TEST_JWT_KEY_ARN"));
        environmentVariables.set("AWS_ACCESS_KEY_ID", getConfig("TEST_ROOT_USER_ACCESS_KEY_ID"));
        environmentVariables.set("AWS_SECRET_ACCESS_KEY", getConfig("TEST_ROOT_USER_SECRET_ACCESS_KEY"));
    }

    @Test
    public void run() throws IOException {
        Path path = Paths.get("src/test/resources/credential-app-output.txt");
        Files.deleteIfExists(path);
        String arg
                = "-r " + getConfig("TEST_AWS_REGION") + " "
                + "-b " + getConfig("TEST_BUCKET") + " "
                + "-u " + getConfig("TEST_USERNAME") + " "
                + "-f " + path.toString();
        CredentialsApp.main(arg.split(" "));
        assertTrue(Files.exists(path));
    }
}

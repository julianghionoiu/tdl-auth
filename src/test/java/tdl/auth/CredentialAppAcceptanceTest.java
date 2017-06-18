package tdl.auth;

import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.EnvironmentVariables;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.Assert.assertTrue;
import static tdl.auth.test.TestConfiguration.*;

public class CredentialAppAcceptanceTest {

    @Rule
    public final EnvironmentVariables environmentVariables = new EnvironmentVariables();

    @Test
    public void run() throws IOException {
        Path path = Paths.get("src/test/resources/credential-app-output.txt");
        Files.deleteIfExists(path);
        String arg
                = "-r " + TEST_AWS_REGION + " "
                + "-b " + TEST_BUCKET + " "
                + "-u " + TEST_USERNAME + " "
                + "-f " + path.toString();
        CredentialsApp.main(arg.split(" "));
        assertTrue(Files.exists(path));
    }
}

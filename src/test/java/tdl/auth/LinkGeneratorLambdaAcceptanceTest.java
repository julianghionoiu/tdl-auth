package tdl.auth;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.EnvironmentVariables;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static tdl.auth.test.TestConfiguration.getConfig;

public class LinkGeneratorLambdaAcceptanceTest {

    @Rule
    public final EnvironmentVariables environmentVariables = new EnvironmentVariables();

    @Before
    public void setUp() {
        environmentVariables.set("AUTH_REGION", getConfig("TEST_AWS_REGION"));
        environmentVariables.set("JWT_ENCRYPT_KEY_ARN", getConfig("TEST_JWT_KEY_ARN"));
        environmentVariables.set("PAGE_STORAGE_BUCKET", getConfig("TEST_BUCKET"));
        environmentVariables.set("AUTH_ENDPOINT_URL", "http://www.example.com/");
        environmentVariables.set("AWS_ACCESS_KEY_ID", getConfig("TEST_ROOT_USER_ACCESS_KEY_ID"));
        environmentVariables.set("AWS_SECRET_ACCESS_KEY", getConfig("TEST_ROOT_USER_SECRET_ACCESS_KEY"));
    }

    @Test
    public void should_generate_link() {
        //environmentVariables.set("AWS_CREDENTIAL_PROFILES_FILE", this.getClass().getResource("/credentials").toString());
        LinkGeneratorLambdaHandler handler = new LinkGeneratorLambdaHandler();
        Map<String, Object> request = new HashMap<>();
        request.put("username", getConfig("TEST_USERNAME"));
        request.put("challenge", "123456");
        request.put("validity", 10);
        String url = handler.handleRequest(request, createMockContext());
        assertThat(url, containsString(getConfig("TEST_BUCKET")));
    }

    private Context createMockContext() {
        Context context = mock(Context.class);
        doReturn(mock(LambdaLogger.class)).when(context).getLogger();
        return context;
    }
}

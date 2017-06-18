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
import static tdl.auth.test.TestConfiguration.*;

public class LinkGeneratorLambdaAcceptanceTest {

    @Rule
    public final EnvironmentVariables environmentVariables = new EnvironmentVariables();
    private LinkGeneratorLambdaHandler handler;

    @Before
    public void setUp() {
        handler = new LinkGeneratorLambdaHandler(
                TEST_AWS_REGION,
                TEST_JWT_KEY_ARN,
                TEST_BUCKET,
                "http://www.example.com/");

    }

    @Test
    public void should_generate_link() {
        //environmentVariables.set("AWS_CREDENTIAL_PROFILES_FILE", this.getClass().getResource("/credentials").toString());
        Map<String, Object> request = new HashMap<>();
        request.put("username", TEST_USERNAME);
        request.put("challenge", "123456");
        request.put("validity", 10);
        String url = handler.handleRequest(request, createMockContext());
        assertThat(url, containsString(TEST_BUCKET));
    }

    private Context createMockContext() {
        Context context = mock(Context.class);
        doReturn(mock(LambdaLogger.class)).when(context).getLogger();
        return context;
    }
}

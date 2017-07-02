package tdl.auth;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.EnvironmentVariables;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.not;
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
        BasicAWSCredentials awsCreds = new BasicAWSCredentials(TEST_ROOT_USER_ACCESS_KEY_ID, TEST_ROOT_USER_SECRET_ACCESS_KEY);
        AWSStaticCredentialsProvider testCredentialsProvider = new AWSStaticCredentialsProvider(awsCreds);
        handler = new LinkGeneratorLambdaHandler(
                TEST_AWS_REGION,
                TEST_JWT_KEY_ARN,
                TEST_PUBLIC_PAGE_BUCKET,
                "http://www.example.com/",
                testCredentialsProvider
        );
    }

    @Test
    public void should_generate_link() {
        Map<String, Object> request = new HashMap<>();
        request.put("username", TEST_USERNAME);
        request.put("validityDays", 10);
        String url = handler.handleRequest(request, createMockContext());
        assertThat(url, containsString(TEST_PUBLIC_PAGE_BUCKET));
        assertThat(url, not(containsString("&Signature="))); //assert that this is from public read bucket
    }

    private Context createMockContext() {
        Context context = mock(Context.class);
        doReturn(mock(LambdaLogger.class)).when(context).getLogger();
        return context;
    }
}

package tdl.auth;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.kms.AWSKMS;
import com.amazonaws.services.kms.AWSKMSClientBuilder;
import com.amazonaws.services.kms.model.DecryptRequest;
import com.amazonaws.services.kms.model.EncryptRequest;
import com.amazonaws.services.lambda.runtime.Context;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import ro.ghionoiu.kmsjwt.key.KMSEncrypt;
import ro.ghionoiu.kmsjwt.key.KeyOperationException;
import ro.ghionoiu.kmsjwt.token.JWTEncoder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertThat;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static tdl.auth.test.TestConfiguration.getConfig;

public class AuthLambdaAcceptanceTest {

    private static final String TEST_AWS_REGION = getConfig("TEST_AWS_REGION");
    private static final String TEST_JWT_KEY_ARN = getConfig("TEST_JWT_KEY_ARN");
    private static final String TEST_BUCKET = getConfig("TEST_BUCKET");
    private static final String TEST_ACCESS_KEY = getConfig("TEST_USER_ACCESS_KEY_ID");
    private static final String TEST_SECRET_KEY = getConfig("TEST_USER_SECRET_KEY_ID");
    private static final String TEST_USERNAME = getConfig("TEST_USERNAME");

    private Context context;
    private AuthLambdaHandler handler;
    private KMSEncrypt kmsEncrypt;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Before
    public void setUp() throws Exception {
        context = mock(Context.class);
        when(context.getLogger()).thenReturn(System.out::println);

        handler = new AuthLambdaHandler(TEST_AWS_REGION, TEST_JWT_KEY_ARN, TEST_BUCKET, TEST_ACCESS_KEY, TEST_SECRET_KEY);

        AWSKMS kmsClient = AWSKMSClientBuilder.standard()
                .withRegion(TEST_AWS_REGION)
                .withCredentials(new AWSStaticCredentialsProvider(
                        new BasicAWSCredentials(TEST_ACCESS_KEY, TEST_SECRET_KEY))
                )
                .build();
        kmsEncrypt = new KMSEncrypt(kmsClient, TEST_JWT_KEY_ARN);
    }

    @SuppressWarnings("SameParameterValue")
    private String getValidToken(String username) throws KeyOperationException {
        return JWTEncoder.builder(kmsEncrypt)
                .claim("usr", username)
                .compact();
    }

    @Test
    public void obtain_temporary_credentials_for_user() throws Exception {
        HashMap<String, Object> input = new HashMap<>();
        input.put("token", getValidToken(TEST_USERNAME));
        input.put("username", TEST_USERNAME);
        JSONObject json = new JSONObject(input);
        //Logger.getLogger("Test").log(Level.INFO, json.toString());
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        handler.handleRequest(asInputStream(json), outputStream, context);

        String credentialsFile = outputStream.toString();
        assertThat(credentialsFile, containsString("Temporary federated credentials"));
        assertThat(credentialsFile, containsString(TEST_USERNAME + "/"));
        assertThat(credentialsFile, containsString("aws_secret_access_key"));
        assertThat(credentialsFile, containsString("aws_access_key_id"));
        assertThat(credentialsFile, containsString("aws_session_token"));
    }

    private ByteArrayInputStream asInputStream(JSONObject json) {
        return new ByteArrayInputStream(json.toString().getBytes(StandardCharsets.UTF_8));
    }
}

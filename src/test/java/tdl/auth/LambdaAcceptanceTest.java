package tdl.auth;

import com.amazonaws.services.kms.AWSKMS;
import com.amazonaws.services.kms.AWSKMSClientBuilder;
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
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class LambdaAcceptanceTest {

    private static final String TEST_AWS_REGION = Optional.ofNullable(System.getenv("TEST_AWS_REGION"))
            .orElse("eu-west-2");
    private static final String TEST_JWT_KEY_ARN = Optional.ofNullable(System.getenv("TEST_JWT_KEY_ARN"))
            .orElse("arn:aws:kms:eu-west-2:577770582757:key/7298331e-c199-4e15-9138-906d1c3d9363");
    private static final String TEST_BUCKET = Optional.ofNullable(System.getenv("TEST_BUCKET"))
            .orElse("testbucket");
    private static final String TEST_ACCESS_KEY = Optional.ofNullable(System.getenv("TEST_ACCESS_KEY"))
            .orElse("ACCESS_KEY");
    private static final String TEST_SECRET_KEY = Optional.ofNullable(System.getenv("TEST_SECRET_KEY"))
            .orElse("SECRET_KEY");

    private Context context;
    private LambdaHandler handler;
    private KMSEncrypt kmsEncrypt;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Before
    public void setUp() throws Exception {
        context = mock(Context.class);
        when(context.getLogger()).thenReturn(System.out::println);

        handler = new LambdaHandler(TEST_AWS_REGION, TEST_JWT_KEY_ARN, TEST_BUCKET, TEST_ACCESS_KEY, TEST_SECRET_KEY);

        AWSKMS kmsClient = AWSKMSClientBuilder.standard()
                .withRegion(TEST_AWS_REGION)
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
        input.put("token", getValidToken("tdl-test-user01"));
        input.put("username", "tdl-test-user01");
        JSONObject json = new JSONObject(input);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        handler.handleRequest(asInputStream(json), outputStream, context);

        String credentialsFile = outputStream.toString();
        assertThat(credentialsFile, containsString("Temporary federated credentials"));
        assertThat(credentialsFile, containsString("tdl-test-user01"+"/"));
        assertThat(credentialsFile, containsString("aws_secret_access_key"));
        assertThat(credentialsFile, containsString("aws_access_key_id"));
        assertThat(credentialsFile, containsString("aws_session_token"));
    }

    private ByteArrayInputStream asInputStream(JSONObject json) {
        return new ByteArrayInputStream(json.toString().getBytes(StandardCharsets.UTF_8));
    }
}

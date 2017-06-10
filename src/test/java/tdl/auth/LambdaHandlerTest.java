package tdl.auth;

import com.amazonaws.services.kms.AWSKMS;
import com.amazonaws.services.kms.AWSKMSClientBuilder;
import com.amazonaws.services.lambda.runtime.Context;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

import com.amazonaws.services.lambda.runtime.LambdaLogger;
import java.util.Optional;
import org.json.JSONObject;

import org.junit.Test;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.ExpectedException;
import ro.ghionoiu.kmsjwt.key.KMSEncrypt;
import ro.ghionoiu.kmsjwt.key.KeyOperationException;
import ro.ghionoiu.kmsjwt.token.JWTEncoder;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.containsString;

public class LambdaHandlerTest {

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

    @Test
    public void testHandler() throws IOException {
        Context context = mock(Context.class);
        when(context.getLogger()).thenReturn(System.out::println);

        HashMap<String, Object> input = new HashMap<>();
        input.put("data", "SGVsbG8gV29ybGQh");
        JSONObject json = new JSONObject(input);

        InputStream inputStream = new ByteArrayInputStream(json.toString().getBytes(StandardCharsets.UTF_8));
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        LambdaHandler handler = new LambdaHandler();

        handler.handleRequest(inputStream, outputStream, context);

        assertEquals(outputStream.toString(), "Hello World!");
    }

    @SuppressWarnings("SameParameterValue")
    private String getValidToken(String username) throws KeyOperationException {
        return JWTEncoder.builder(kmsEncrypt)
                .claim("usr", username)
                .compact();
    }

    @Test
    public void accepts_valid_token() throws IOException, KeyOperationException {
        HashMap<String, Object> input = new HashMap<>();
        input.put("token", getValidToken("test-user"));
        input.put("username", "test-user");
        JSONObject json = new JSONObject(input);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        handler.handleRequest(asInputStream(json), outputStream, context);
        assertThat(outputStream.toString(), is("theFile"));
    }

    @Test
    public void rejects_bad_input() throws IOException, KeyOperationException {
        HashMap<String, Object> input = new HashMap<>();
        JSONObject json = new JSONObject(input);

        expectedException.expectMessage(containsString("[Input]"));
        handler.handleRequest(asInputStream(json), new ByteArrayOutputStream(), context);
    }

    @Test
    public void rejects_malformed_token() throws IOException, KeyOperationException {
        HashMap<String, Object> input = new HashMap<>();
        input.put("token", "XYZ");
        input.put("username", "test-userX");
        JSONObject json = new JSONObject(input);

        expectedException.expectMessage(containsString("[Verification]"));
        handler.handleRequest(asInputStream(json), new ByteArrayOutputStream(), context);
    }

    @Test
    public void rejects_token_that_does_not_match_user() throws IOException, KeyOperationException {
        HashMap<String, Object> input = new HashMap<>();
        input.put("token", getValidToken("test-user"));
        input.put("username", "other-user");
        JSONObject json = new JSONObject(input);

        expectedException.expectMessage(containsString("[Authorization]"));
        handler.handleRequest(asInputStream(json), new ByteArrayOutputStream(), context);
    }

    private ByteArrayInputStream asInputStream(JSONObject json) {
        return new ByteArrayInputStream(json.toString().getBytes(StandardCharsets.UTF_8));
    }
}

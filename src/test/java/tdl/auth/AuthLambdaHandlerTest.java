package tdl.auth;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.securitytoken.model.Credentials;
import io.jsonwebtoken.impl.DefaultClaims;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import tdl.auth.authorizer.AuthenticationException;
import tdl.auth.authorizer.AuthorizationException;
import tdl.auth.authorizer.LambdaAuthorizer;
import tdl.auth.federated.FederatedUserCredentials;
import tdl.auth.federated.FederatedUserCredentialsProvider;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

public class AuthLambdaHandlerTest {
    private AuthLambdaHandler lambdaHandler;
    private FederatedUserCredentialsProvider temporaryCredentialsProvider;
    private LambdaAuthorizer lambdaAuthorizer;
    private Context context;

    private ByteArrayOutputStream outputStream;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Before
    public void setUp() {
        temporaryCredentialsProvider = mock(FederatedUserCredentialsProvider.class);
        lambdaAuthorizer = mock(LambdaAuthorizer.class);
        lambdaHandler = new AuthLambdaHandler(temporaryCredentialsProvider, lambdaAuthorizer);
        context = mock(Context.class);
        when(context.getLogger()).thenReturn(System.out::println);
        outputStream = new ByteArrayOutputStream();
    }

    @Test
    public void generates_credentials_file_for_valid_token() throws Exception {
        when(lambdaAuthorizer.getClaims(eq("test-user"), eq("test-challenge"), eq("token")))
                .thenReturn(new DefaultClaims());
        when(temporaryCredentialsProvider.getFederatedTokenFor(eq("test-challenge"), eq("test-user")))
                .thenReturn(validCredentials());

        lambdaHandler.handleRequest(jsonPayloadAsStream("test-user",
                "test-challenge", "token"),
                outputStream, context);

        String credentialsFile = outputStream.toString();
        assertThat(credentialsFile, containsString("expectedRegion"));
        assertThat(credentialsFile, containsString("expectedBucket"));
        assertThat(credentialsFile, containsString("expectedS3Prefix" + "/"));
        assertThat(credentialsFile, containsString("expectedKeyId"));
        assertThat(credentialsFile, containsString("expectedSecretKey"));
        assertThat(credentialsFile, containsString("expectedSessionToken"));
    }

    @Test
    public void detects_authentication_error() throws Exception {
        doThrow(new AuthenticationException("failed", new Exception()))
                .when(lambdaAuthorizer).getClaims(anyString(), anyString(), anyString());

        expectedException.expectMessage(containsString("[Authentication]"));
        lambdaHandler.handleRequest(jsonPayloadAsStream("test-user",
                "test-challenge", "token"),
                outputStream, context);
    }

    @Test
    public void detects_authorization_error() throws Exception {
        when(lambdaAuthorizer.getClaims(anyString(), anyString(), anyString()))
                .thenThrow(new AuthorizationException("Not allowed"));

        expectedException.expectMessage(containsString("[Authorization]"));
        lambdaHandler.handleRequest(jsonPayloadAsStream("test-user",
                "test-challenge", "token"),
                outputStream, context);
    }

    @Test
    public void detects_invalid_input() throws Exception {
        expectedException.expectMessage(containsString("[Input]"));
        lambdaHandler.handleRequest(asStream("invalidJson"), outputStream, context);
    }


    private static FederatedUserCredentials validCredentials() {
        return new FederatedUserCredentials(
                "expectedRegion",
                "expectedBucket",
                "expectedS3Prefix",
                new Credentials("expectedKeyId",
                        "expectedSecretKey",
                        "expectedSessionToken",
                        null));
    }


    @SuppressWarnings("SameParameterValue")
    private ByteArrayInputStream jsonPayloadAsStream(String username, String challenge, String token) {
        HashMap<String, Object> input = new HashMap<>();
        input.put("token", token);
        input.put("username", username);
        input.put("challenge", challenge);
        JSONObject json = new JSONObject(input);
        return asStream(json.toString());
    }

    private ByteArrayInputStream asStream(String s) {
        return new ByteArrayInputStream(s.getBytes(StandardCharsets.UTF_8));
    }
}

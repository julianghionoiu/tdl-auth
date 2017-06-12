package tdl.auth;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import org.json.JSONException;
import org.json.JSONObject;
import tdl.auth.authorizer.AuthenticationException;
import tdl.auth.authorizer.AuthorizationException;
import tdl.auth.authorizer.JWTKMSAuthorizer;
import tdl.auth.authorizer.LambdaAuthorizer;
import tdl.auth.federated.FederatedUserCredentials;
import tdl.auth.federated.FederatedUserCredentialsProvider;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class AuthLambdaHandler implements RequestStreamHandler {

    private final FederatedUserCredentialsProvider temporaryCredentialsProvider;
    private final LambdaAuthorizer lambdaAuthorizer;

    private static String getEnv(String key) {
        return Optional.ofNullable(System.getenv(key))
                .orElseThrow(()
                        -> new RuntimeException("[Startup] Environment variable " + key + " not set"));
    }

    @SuppressWarnings("unused")
    public AuthLambdaHandler() {
        this(
                getEnv("REGION"),
                getEnv("JWT_DECRYPT_KEY_ARN"),
                getEnv("BUCKET"),
                getEnv("ACCESS_KEY"),
                getEnv("SECRET_KEY") //TODO: Encrypt this using KMS.
        );
    }

    /**
     * A Lambda has temporary credentials obtained by calling AssumeRole on the
     * Execution Role, so we need to use IAM user.
     */
    AuthLambdaHandler(String region, String jwtDecryptKeyARN, String bucket, String accessKey, String secretKey) {
        BasicAWSCredentials awsCreds = new BasicAWSCredentials(accessKey, secretKey);
        AWSCredentialsProvider awsCredential = new AWSStaticCredentialsProvider(awsCreds);
        temporaryCredentialsProvider = new FederatedUserCredentialsProvider(region, bucket, awsCredential);
        lambdaAuthorizer = new JWTKMSAuthorizer(region, jwtDecryptKeyARN);
    }

    AuthLambdaHandler(FederatedUserCredentialsProvider temporaryCredentialsProvider, LambdaAuthorizer lambdaAuthorizer) {
        this.temporaryCredentialsProvider = temporaryCredentialsProvider;
        this.lambdaAuthorizer = lambdaAuthorizer;
    }

    @Override
    public void handleRequest(InputStream inputStream, OutputStream outputStream, Context context) throws IOException {
        Optional<RuntimeException> exception = Optional.empty();
        try {
            doHandleRequest(inputStream, outputStream, context);
        } catch (AuthorizationException e) {
            exception = Optional.of(new RuntimeException("[Authorization] " + e.getMessage(), e));
        } catch (AuthenticationException e) {
            exception = Optional.of(new RuntimeException("[Authentication] " + e.getMessage(), e));
        } catch (JSONException e) {
            exception = Optional.of(new RuntimeException("[Input] " + e.getMessage(), e));
        } catch (Exception e) {
            exception = Optional.of(new RuntimeException("[UnknownException] " + e.getMessage(), e));
        }

        if (exception.isPresent()) {
            Exception e = exception.get();

            // Collect all stack traces
            List<String> theTrace = new ArrayList<>();
            {
                Throwable ex = e;
                while (ex != null) {
                    theTrace.add(ex.getClass().getSimpleName() + ": " + ex.getMessage());
                    StackTraceElement[] stackTrace = ex.getStackTrace();
                    Arrays.stream(stackTrace).map(StackTraceElement::toString).forEach(theTrace::add);
                    ex = ex.getCause();
                }
            }

            //Log the stack traces
            context.getLogger().log(theTrace.stream().collect(Collectors.joining("\n")));

            // Return the message to the user
            throw new IOException(e.getMessage());
        }
    }

    private void doHandleRequest(InputStream inputStream, OutputStream outputStream, Context context) throws AuthenticationException, IOException, AuthorizationException {
        // Get json input
        String inputJson = getStringInput(inputStream);
        context.getLogger().log("inputJson:" + inputJson);

        // Parse JSON
        JSONObject json = new JSONObject(inputJson);
        String username = json.getString("username");
        String token = json.getString("token");
        context.getLogger().log("username:" + username + ", token:" + token);

        // Authorize
        boolean isAuthorized = lambdaAuthorizer.isAuthorized(username, token);
        if (!isAuthorized) {
            throw new AuthorizationException("[Authorization] User not authorized to perform action");
        }

        // Generate credentials
        context.getLogger().log("providers:" + temporaryCredentialsProvider.getBucket() + " " + temporaryCredentialsProvider.getRegion());

        FederatedUserCredentials temporaryCredentials = temporaryCredentialsProvider.getFederatedTokenFor(username);
        context.getLogger().log("temporary credentials generated:" + temporaryCredentials);

        // Return as file
        temporaryCredentials.saveTo(outputStream);
    }

    private static String getStringInput(InputStream inputStream) {
        return new BufferedReader(new InputStreamReader(inputStream))
                .lines()
                .collect(Collectors.joining(""));
    }
}

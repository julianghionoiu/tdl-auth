package tdl.auth;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import io.jsonwebtoken.Claims;
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

import tdl.auth.helpers.JWTTdlTokenUtils;
import tdl.auth.helpers.JourneyIdUtils;
import tdl.auth.helpers.LambdaExceptionLogger;

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
                getEnv("TDL_SCOPE"),
                getEnv("ACCESS_KEY"),
                getEnv("SECRET_KEY") //TODO: Encrypt this using KMS.
        );
    }

    /**
     * A Lambda has temporary credentials obtained by calling AssumeRole on the
     * Execution Role, so we need to use IAM user.
     */
    AuthLambdaHandler(String region, String jwtDecryptKeyARN, String bucket, String scope, String accessKey, String secretKey) {
        BasicAWSCredentials awsCreds = new BasicAWSCredentials(accessKey, secretKey);
        AWSCredentialsProvider awsCredential = new AWSStaticCredentialsProvider(awsCreds);
        temporaryCredentialsProvider = new FederatedUserCredentialsProvider(region, bucket, scope, awsCredential);
        lambdaAuthorizer = new JWTKMSAuthorizer(region, jwtDecryptKeyARN, awsCredential);
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
            LambdaExceptionLogger.logException(context, e);
            // Return the message to the user
            throw new IOException(e.getMessage(), e);
        }
    }

    private void doHandleRequest(InputStream inputStream, OutputStream outputStream, Context context) throws AuthenticationException, IOException, AuthorizationException {
        // Get json input
        String inputJson = getStringInput(inputStream);
        context.getLogger().log("inputJson:" + inputJson);

        // Parse JSON
        JSONObject json = new JSONObject(inputJson);
        String username = json.getString("username");
        String officialChallenge = json.getString("challenge");
        String token = json.getString("token");
        context.getLogger().log(String.format("challenge:%s, username:%s, token:%s", officialChallenge, username, token));

        // Authorize and get claims
        Claims claims = lambdaAuthorizer.getClaims(username, officialChallenge, token);
        List<String> warmupChallenges = JWTTdlTokenUtils.getWarmupChallenges(claims);

        // Generate credentials
        context.getLogger().log("providers:" + temporaryCredentialsProvider.getBucket() + " " + temporaryCredentialsProvider.getRegion());

        FederatedUserCredentials temporaryCredentials = temporaryCredentialsProvider.getFederatedTokenFor(officialChallenge, username);
        context.getLogger().log("temporary credentials generated:" + temporaryCredentials);

        // Write Config file
        temporaryCredentials.saveTo(outputStream);
        RunnerConfiguration.of(username, warmupChallenges, officialChallenge).saveTo(outputStream);
    }

    private static String getStringInput(InputStream inputStream) {
        return new BufferedReader(new InputStreamReader(inputStream))
                .lines()
                .collect(Collectors.joining(""));
    }

    private static class RunnerConfiguration {
        private final String username;
        private final List<String> warmupChallenges;
        private final String officialChallenge;

        RunnerConfiguration(String username, List<String> warmupChallenges, String officialChallenge) {
            this.username = username;
            this.warmupChallenges = warmupChallenges;
            this.officialChallenge = officialChallenge;
        }

        static RunnerConfiguration of(String username, List<String> warmupChallenges, String officialChallenge) {
            return new RunnerConfiguration(username, warmupChallenges, officialChallenge);
        }

        void saveTo(OutputStream outputStream) throws IOException {
            Properties properties = new Properties();
            properties.setProperty("tdl_username", username);
            properties.setProperty("tdl_request_queue_name", username+".req");
            properties.setProperty("tdl_response_queue_name", username+".resp");
            properties.setProperty("tdl_hostname", "run.befaster.io");
            properties.setProperty("tdl_require_rec", "true");
            properties.setProperty("tdl_journey_id", JourneyIdUtils.encode(username, warmupChallenges, officialChallenge));
            properties.setProperty("tdl_use_coloured_output", "true");
            properties.setProperty("tdl_enable_experimental", "true");
            properties.store(outputStream, " Runner specific configuration ");
        }
    }
}

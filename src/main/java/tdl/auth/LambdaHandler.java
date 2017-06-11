package tdl.auth;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import org.json.JSONException;
import org.json.JSONObject;
import tdl.auth.authorizer.AuthorizationException;
import tdl.auth.authorizer.JWTKMSAuthorizer;
import tdl.auth.authorizer.LambdaAuthorizer;
import tdl.auth.federated.FederatedUserCredentials;
import tdl.auth.federated.FederatedUserCredentialsProvider;

import java.io.*;
import java.util.Optional;
import java.util.stream.Collectors;

public class LambdaHandler implements RequestStreamHandler {

    private final FederatedUserCredentialsProvider temporaryCredentialsProvider;
    private final LambdaAuthorizer lambdaAuthorizer;

    private static String getEnv(String key) {
        return Optional.ofNullable(System.getenv(key))
                .orElseThrow(() ->
                    new RuntimeException("[Startup] Environment variable " + key + " not set"));
    }

    @SuppressWarnings("unused")
    public LambdaHandler() {
        this(
                getEnv("REGION"),
                getEnv("JWT_DECRYPT_KEY_ARN"),
                getEnv("BUCKET"),
                getEnv("ACCESS_KEY"),
                getEnv("SECRET_KEY") //TODO: Encrypt this using KMS.
        );

    }

    /**
     * A Lambda has temporary credentials obtained by calling AssumeRole on
     * the Execution Role, so we need to use IAM user.
     */
    LambdaHandler(String region, String jwtDecryptKeyARN, String bucket, String accessKey, String secretKey) {
        BasicAWSCredentials awsCreds = new BasicAWSCredentials(accessKey, secretKey);
        AWSCredentialsProvider awsCredential = new AWSStaticCredentialsProvider(awsCreds);
        temporaryCredentialsProvider = new FederatedUserCredentialsProvider(region, bucket, awsCredential);
        lambdaAuthorizer = new JWTKMSAuthorizer(region, jwtDecryptKeyARN);
    }

    LambdaHandler(FederatedUserCredentialsProvider temporaryCredentialsProvider, LambdaAuthorizer lambdaAuthorizer) {
        this.temporaryCredentialsProvider = temporaryCredentialsProvider;
        this.lambdaAuthorizer = lambdaAuthorizer;
    }

    @Override
    public void handleRequest(InputStream inputStream, OutputStream outputStream, Context context) throws IOException {
        try {
            // Get json input
            String inputJson = getStringInput(inputStream);
            context.getLogger().log("inputJson:" + inputJson);

            // Parse JSON
            JSONObject json = new JSONObject(inputJson);
            String username = json.getString("username");
            String token = json.getString("token");
            context.getLogger().log("username:" + username+", token:"+token);

            // Authorize
            boolean isAuthorized = lambdaAuthorizer.isAuthorized(username, token);
            if (!isAuthorized) {
                throw new RuntimeException("[Authorization] User not authorized to perform action");
            }

            // Generate credentials
            context.getLogger().log("providers:" + temporaryCredentialsProvider.getBucket() + " " + temporaryCredentialsProvider.getRegion());

            FederatedUserCredentials temporaryCredentials = temporaryCredentialsProvider.getFederatedTokenFor(username);
            context.getLogger().log("temporary credentials generated:" + temporaryCredentials);

            // Return as file
            temporaryCredentials.saveTo(outputStream);
        } catch (AuthorizationException e) {
            throw new RuntimeException("[Verification] " + e.getMessage(), e);
        } catch (JSONException e) {
            throw new RuntimeException("[Input] " + e.getMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    private static String getStringInput(InputStream inputStream) {
        return new BufferedReader(new InputStreamReader(inputStream))
                .lines()
                .collect(Collectors.joining(""));
    }
}

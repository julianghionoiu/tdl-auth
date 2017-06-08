package tdl.auth;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.EnvironmentVariableCredentialsProvider;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;
import tdl.auth.federated.FederatedUserCredentials;
import tdl.auth.federated.FederatedUserCredentialsProvider;
import tdl.auth.lambda.CredentialInput;

public class LambdaHandler implements RequestStreamHandler {

    private final FederatedUserCredentialsProvider credentialsProvider;

    @SuppressWarnings("unused")
    public LambdaHandler() {
        this(
                System.getenv("REGION"),
                System.getenv("BUCKET"),
                System.getenv("ACCESS_KEY"),
                System.getenv("SECRET_KEY") //TODO: Encrypt this using KMS.
        );

    }

    LambdaHandler(String region, String bucket, String accessKey, String secretKey) {
        /**
         * A Lambda has temporary credentials obtained by calling AssumeRole on
         * the Execution Role, so we need to use IAM user.
         */
        BasicAWSCredentials awsCreds = new BasicAWSCredentials(accessKey, secretKey);
        AWSCredentialsProvider awsCredential = new AWSStaticCredentialsProvider(awsCreds);
        credentialsProvider = new FederatedUserCredentialsProvider(region, bucket, awsCredential);
    }

    @Override
    public void handleRequest(InputStream inputStream, OutputStream outputStream, Context context) throws IOException {
        try {
            String inputJson = getStringInput(inputStream);
            context.getLogger().log("inputJson:" + inputJson);
            context.getLogger().log("providers:" + credentialsProvider.getBucket() + " " + credentialsProvider.getRegion());
            CredentialInput credentialInput = CredentialInput.createFromJsonString(inputJson);
            context.getLogger().log("input:" + credentialInput);
            FederatedUserCredentials credentials = createCredentials(credentialInput);
            context.getLogger().log("credentials:" + credentials);
            credentials.save(outputStream);
        } catch (Exception e) {
            context.getLogger().log("Exception " + e.getClass().getSimpleName() + ": " + e.getMessage());
        }
    }

    private static String getStringInput(InputStream inputStream) {
        return new BufferedReader(new InputStreamReader(inputStream))
                .lines()
                .collect(Collectors.joining(""));
    }

    private FederatedUserCredentials createCredentials(CredentialInput input) {
        return new FederatedUserCredentials(credentialsProvider, input.username);
    }

}

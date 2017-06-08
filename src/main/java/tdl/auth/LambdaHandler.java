package tdl.auth;

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
        this(System.getenv("REGION"), System.getenv("BUCKET"));
    }

    LambdaHandler(String region, String bucket) {
        credentialsProvider = new FederatedUserCredentialsProvider(region, bucket);
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
            outputStream.write(credentialInput.toString().getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            context.getLogger().log("Exception "+e.getClass().getSimpleName()+": "+e.getMessage());
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

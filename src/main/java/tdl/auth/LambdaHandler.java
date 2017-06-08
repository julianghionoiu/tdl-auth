package tdl.auth;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;

import java.io.*;
import java.util.stream.Collectors;
import tdl.auth.federated.FederatedUserCredentials;
import tdl.auth.lambda.CredentialInput;

public class LambdaHandler implements RequestStreamHandler {

    @Override
    public void handleRequest(InputStream inputStream, OutputStream outputStream, Context context) throws IOException {
        try {
            String inputJson = getStringInput(inputStream);
            context.getLogger().log("inputJson:" + inputJson);
            CredentialInput credentialInput = CredentialInput.createFromJsonString(inputJson);
            FederatedUserCredentials credentials = createCredentials(credentialInput);
            context.getLogger().log("credential:" + credentialInput);
            credentials.save(outputStream);
            //outputStream.write(credentialInput.toString().getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String getStringInput(InputStream inputStream) {
        return new BufferedReader(new InputStreamReader(inputStream))
                .lines()
                .collect(Collectors.joining(""));
    }

    private static FederatedUserCredentials createCredentials(CredentialInput input) {
        String bucket = System.getenv("BUCKET");
        String region = System.getenv("REGION");
        return new FederatedUserCredentials(bucket, region, input.username);
    }

}

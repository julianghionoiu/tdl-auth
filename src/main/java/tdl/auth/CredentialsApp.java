package tdl.auth;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import java.io.FileOutputStream;
import lombok.extern.slf4j.Slf4j;

import tdl.auth.federated.FederatedUserCredentials;
import tdl.auth.federated.FederatedUserCredentialsProvider;

@Slf4j
public class CredentialsApp {

    @Parameter(names = {"-r", "--region"}, description = "The region where the bucket lives", required = true)
    private String region;

    @Parameter(names = {"-b", "--bucket"}, description = "Name of the bucket to generate permissions for", required = true)
    private String bucket;

    @Parameter(names = {"-s", "--scope"}, description = "Name scope of the bucket, username restriction (test/live)", required = true)
    private String scope;

    @Parameter(names = {"-c", "--challenge"}, description = "Challenge to generate permissions for", required = true)
    private String challenge;

    @Parameter(names = {"-u", "--username"}, description = "Username to generate permissions for", required = true)
    private String username;

    @Parameter(names = {"-f", "--file"}, description = "The file to save temporal credentials", required = true)
    private String fileToSave = ".private/aws-temp-secrets";

    public static void main(String[] args) {
        log.info("Retrieving temporary credentials");
        CredentialsApp main = new CredentialsApp();
        new JCommander(main, args);
        main.run();
        log.info("Credentials saved to file " + main.fileToSave);
    }

    private void run() {
        try {
            FederatedUserCredentialsProvider credentialsProvider = new FederatedUserCredentialsProvider(region, bucket, scope);

            FederatedUserCredentials credentials = credentialsProvider.getFederatedTokenFor(challenge, username);

            try (FileOutputStream output = new FileOutputStream(fileToSave)) {
                credentials.saveTo(output);
            }
        } catch (Exception e) {
            log.error("Exception encountered.", e);
        }
    }
}

package tdl.auth;

import com.amazonaws.services.securitytoken.model.Credentials;
import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import lombok.extern.slf4j.Slf4j;
import tdl.auth.federated.FederatedUserCredentialsProvider;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;

@Slf4j
public class CredentialsApp {

    @Parameter(names = {"-r", "--region"}, description = "The region where the bucket lives", required = true)
    private String region;

    @Parameter(names = {"-b", "--bucket"}, description = "Name of the bucket to generate permissions for", required = true)
    private String bucket;

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
            FederatedUserCredentialsProvider temporalCredentialsProvider =  new FederatedUserCredentialsProvider(region, bucket);

            Credentials credentials = temporalCredentialsProvider.getTokenFor(username).getCredentials();
            saveCredentials(credentials, region, bucket, username);
        } catch (Exception e) {
            log.error("Exception encountered.", e);
        }
    }

    private void saveCredentials(Credentials credentials, String s3Region, String s3Bucket, String username) throws IOException {
        try (FileWriter writer = new FileWriter(fileToSave)) {
            Properties properties = new Properties();
            properties.setProperty("aws_access_key_id", credentials.getAccessKeyId());
            properties.setProperty("aws_secret_access_key", credentials.getSecretAccessKey());
            properties.setProperty("aws_session_token", credentials.getSessionToken());
            properties.setProperty("s3_region", s3Region);
            properties.setProperty("s3_bucket", s3Bucket);
            properties.setProperty("s3_prefix", username+"/");
            properties.store(writer, "Temporary federated credentials");
        }
    }
}

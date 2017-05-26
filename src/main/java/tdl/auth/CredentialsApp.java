package tdl.auth;

import com.amazonaws.auth.AWSSessionCredentials;
import com.amazonaws.auth.policy.Policy;
import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import lombok.extern.slf4j.Slf4j;
import tdl.auth.credentials.AWSSecretPropertiesCredentialsProvider;
import tdl.auth.federated.FederatedUserCredentialsProvider;
import tdl.auth.s3.DefaultS3FolderPolicy;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Properties;

@Slf4j
public class CredentialsApp {

    @Parameter(names = {"-c", "--config"}, description = "The file containing the AWS parameters")
    private String configFile = ".private/aws-test-secrets";

    @Parameter(names = {"-f", "--file"}, description = "The file to save temporal credentials")
    private String fileToSave = ".private/aws-temp-secrets";

    private AWSSecretPropertiesCredentialsProvider securityProperties;

    public static void main(String[] args) {
        log.info("Retrieving temporal credentials");
        CredentialsApp main = new CredentialsApp();
        new JCommander(main, args);
        main.run();
        log.info("Credentials saved to file " + main.fileToSave);
    }

    public void run() {
        try {
            securityProperties = AWSSecretPropertiesCredentialsProvider.fromPlainTextFile(Paths.get(configFile));

            Policy policy = DefaultS3FolderPolicy.getForUser(securityProperties.getS3Bucket(), securityProperties.getS3Prefix());

            FederatedUserCredentialsProvider temporalCredentialsProvider =  FederatedUserCredentialsProvider.builder()
                    .iamUserCredentialsProvider(securityProperties)
                    .region(securityProperties.getS3Region())
                    .userName(securityProperties.getS3Prefix())
                    .policy(policy)
                    .build();

            saveCredentials(temporalCredentialsProvider.getCredentials());
        } catch (Exception e) {
            log.error("Exception encountered.", e);
        }
    }

    private void saveCredentials(AWSSessionCredentials credentials) throws IOException {
        try (FileWriter writer = new FileWriter(fileToSave)) {
            Properties properties = new Properties();
            properties.setProperty("aws_access_key_id", credentials.getAWSAccessKeyId());
            properties.setProperty("aws_secret_access_key", credentials.getAWSSecretKey());
            properties.setProperty("aws_session_token", credentials.getSessionToken());
            properties.setProperty("s3_region", securityProperties.getS3Region());
            properties.setProperty("s3_bucket", securityProperties.getS3Bucket());
            properties.setProperty("s3_prefix", securityProperties.getS3Prefix());
            properties.store(writer, "temporal credentials properties");
        }
    }
}

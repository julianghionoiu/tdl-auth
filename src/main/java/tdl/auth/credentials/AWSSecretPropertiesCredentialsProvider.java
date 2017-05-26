package tdl.auth.credentials;

import com.amazonaws.auth.*;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

/**
 * Read credentials and bucket information from private properties file.
 * <p>
 * The file should contain the following keys:
 * - aws_access_key_id
 * - aws_secret_access_key
 * - s3_region
 * - s3_bucket
 */
public class AWSSecretPropertiesCredentialsProvider implements AWSCredentialsProvider {
    private Properties privateProperties;

    private AWSSecretPropertiesCredentialsProvider(Properties privateProperties) {
        this.privateProperties = privateProperties;
    }

    public static AWSSecretPropertiesCredentialsProvider fromPlainTextFile(Path plainTextPropertyFile) {
        return new AWSSecretPropertiesCredentialsProvider(loadPrivateProperties(plainTextPropertyFile));
    }

    @Override
    public void refresh() {

    }


    @Override
    public AWSCredentials getCredentials() {
        String awsAccessKeyId = privateProperties.getProperty("aws_access_key_id");
        String awsSecretAccessKey = privateProperties.getProperty("aws_secret_access_key");
        String awsSessionToken = privateProperties.getProperty("aws_session_token");

        return awsSessionToken != null ?
                new BasicSessionCredentials(awsAccessKeyId, awsSecretAccessKey, awsSessionToken)
                :
                new BasicAWSCredentials(awsAccessKeyId, awsSecretAccessKey);
    }


    public String getS3Region() {
        return privateProperties.getProperty("s3_region");
    }

    public String getS3Bucket() {
        return privateProperties.getProperty("s3_bucket");
    }

    public String getS3Prefix() {
        return privateProperties.getProperty("s3_prefix");
    }

    private static Properties loadPrivateProperties(Path privatePropertiesPath) {
        Properties properties = new Properties();
        try (InputStream inStream = Files.newInputStream(privatePropertiesPath)) {
            properties.load(inStream);
            return properties;
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
}

package tdl.auth.federated;

import com.amazonaws.services.securitytoken.model.Credentials;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Properties;

public class FederatedUserCredentials {

    public final String bucket;

    public final String region;

    public final String username;

    private Credentials credentials;

    private Properties properties;

    public FederatedUserCredentials(String bucket, String region, String username) {
        this.bucket = bucket;
        this.region = region;
        this.username = username;
        createCredentials();
        storeProperties();
    }

    private void createCredentials() {
        FederatedUserCredentialsProvider provider = new FederatedUserCredentialsProvider(region, bucket);

        credentials = provider
                .getTokenFor(username)
                .getCredentials();
    }

    private void storeProperties() {
        properties = new Properties();
        properties.setProperty("aws_access_key_id", credentials.getAccessKeyId());
        properties.setProperty("aws_secret_access_key", credentials.getSecretAccessKey());
        properties.setProperty("aws_session_token", credentials.getSessionToken());
        properties.setProperty("s3_region", region);
        properties.setProperty("s3_bucket", bucket);
        properties.setProperty("s3_prefix", username + "/");
    }

    public void save(OutputStream outputStream) throws IOException {
        properties.store(outputStream, "Temporary federated credentials");
    }
}

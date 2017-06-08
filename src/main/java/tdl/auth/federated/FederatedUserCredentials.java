package tdl.auth.federated;

import com.amazonaws.services.securitytoken.model.Credentials;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Properties;

public class FederatedUserCredentials {

    public final String username;

    private final Credentials credentials;

    private final Properties properties;

    private final FederatedUserCredentialsProvider provider;

    public FederatedUserCredentials(FederatedUserCredentialsProvider provider, String username) {
        this.username = username;
        this.provider = provider;
        credentials = provider
                .getTokenFor(username)
                .getCredentials();
        properties = new Properties();
        storeProperties();
    }

    private void storeProperties() {
        properties.setProperty("aws_access_key_id", credentials.getAccessKeyId());
        properties.setProperty("aws_secret_access_key", credentials.getSecretAccessKey());
        properties.setProperty("aws_session_token", credentials.getSessionToken());
        properties.setProperty("s3_region", provider.getRegion());
        properties.setProperty("s3_bucket", provider.getBucket());
        properties.setProperty("s3_prefix", username + "/");
    }

    public void save(OutputStream outputStream) throws IOException {
        properties.store(outputStream, "Temporary federated credentials");
    }

    @Override
    public String toString() {
        return properties.toString();
    }
}

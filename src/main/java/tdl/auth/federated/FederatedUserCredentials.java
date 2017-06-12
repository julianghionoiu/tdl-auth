package tdl.auth.federated;

import com.amazonaws.services.securitytoken.model.Credentials;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Properties;

public class FederatedUserCredentials {

    private final String region;
    private final String bucket;
    private final String username;
    private final Credentials credentials;


    public FederatedUserCredentials(String region, String bucket, String username, Credentials credentials) {
        this.region = region;
        this.bucket = bucket;
        this.username = username;
        this.credentials = credentials;
    }

    Credentials getCredentials() {
        return credentials;
    }

    public void saveTo(OutputStream outputStream) throws IOException {
        Properties properties = new Properties();
        properties.setProperty("aws_access_key_id", credentials.getAccessKeyId());
        properties.setProperty("aws_secret_access_key", credentials.getSecretAccessKey());
        properties.setProperty("aws_session_token", credentials.getSessionToken());
        properties.setProperty("s3_region", region);
        properties.setProperty("s3_bucket", bucket);
        properties.setProperty("s3_prefix", username + "/");
        properties.store(outputStream, "Temporary federated credentials");
    }


    @Override
    public String toString() {
        return "FederatedUserCredentials{" +
                "username='" + username + '\'' +
                ", credentials=<secret>" +
                ", region='" + region + '\'' +
                ", bucket='" + bucket + '\'' +
                '}';
    }
}

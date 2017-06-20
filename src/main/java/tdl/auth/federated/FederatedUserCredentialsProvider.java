package tdl.auth.federated;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.policy.Policy;
import com.amazonaws.services.securitytoken.AWSSecurityTokenService;
import com.amazonaws.services.securitytoken.AWSSecurityTokenServiceClientBuilder;
import com.amazonaws.services.securitytoken.model.GetFederationTokenRequest;
import com.amazonaws.services.securitytoken.model.GetFederationTokenResult;

import java.util.concurrent.TimeUnit;

public class FederatedUserCredentialsProvider {
    private static final int TEMPORARY_CREDENTIALS_VALIDITY = (int) TimeUnit.HOURS.toSeconds(24);

    private final AWSSecurityTokenService tokenService;

    private final String bucket;

    private final String region;

    public FederatedUserCredentialsProvider(String region, String bucket) {
        tokenService = AWSSecurityTokenServiceClientBuilder
                .standard()
                .withRegion(region)
                .build();
        this.bucket = bucket;
        this.region = region;
    }
    
    public FederatedUserCredentialsProvider(String region, String bucket, AWSCredentialsProvider credentialsProvider) {
        tokenService = AWSSecurityTokenServiceClientBuilder
                .standard()
                .withCredentials(credentialsProvider)
                .withRegion(region)
                .build();
        this.bucket = bucket;
        this.region = region;
    }

    public FederatedUserCredentials getFederatedTokenFor(String username) {
        Policy policy = DefaultS3FolderPolicy.getForUser(bucket, username);
        GetFederationTokenRequest getFederationTokenRequest = new GetFederationTokenRequest()
                .withName(username)
                .withDurationSeconds(TEMPORARY_CREDENTIALS_VALIDITY)
                .withPolicy(policy.toJson());
        GetFederationTokenResult federationTokenResult = tokenService.getFederationToken(getFederationTokenRequest);
        return new FederatedUserCredentials(region, bucket, username, federationTokenResult.getCredentials());
    }

    public String getBucket() {
        return bucket;
    }

    public String getRegion() {
        return region;
    }

}

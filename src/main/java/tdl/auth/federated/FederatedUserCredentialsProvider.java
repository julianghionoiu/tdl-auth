package tdl.auth.federated;

import com.amazonaws.auth.policy.Policy;
import com.amazonaws.services.securitytoken.AWSSecurityTokenService;
import com.amazonaws.services.securitytoken.AWSSecurityTokenServiceClientBuilder;
import com.amazonaws.services.securitytoken.model.GetFederationTokenRequest;
import com.amazonaws.services.securitytoken.model.GetFederationTokenResult;
import tdl.auth.s3.DefaultS3FolderPolicy;

public class FederatedUserCredentialsProvider {

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

    public GetFederationTokenResult getTokenFor(String username) {
        Policy policy = DefaultS3FolderPolicy.getForUser(bucket, username);
        GetFederationTokenRequest getFederationTokenRequest = new GetFederationTokenRequest()
                .withName(username)
                .withPolicy(policy.toJson());
        return tokenService.getFederationToken(getFederationTokenRequest);
    }

    public String getBucket() {
        return bucket;
    }

    public String getRegion() {
        return region;
    }

}

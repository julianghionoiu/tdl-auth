package tdl.auth.federated;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSSessionCredentials;
import com.amazonaws.auth.BasicSessionCredentials;
import com.amazonaws.auth.policy.Policy;
import com.amazonaws.services.securitytoken.AWSSecurityTokenService;
import com.amazonaws.services.securitytoken.AWSSecurityTokenServiceClientBuilder;
import com.amazonaws.services.securitytoken.model.Credentials;
import com.amazonaws.services.securitytoken.model.GetFederationTokenRequest;
import com.amazonaws.services.securitytoken.model.GetFederationTokenResult;
import lombok.Builder;

import java.time.Instant;

public class FederatedUserCredentialsProvider implements AWSCredentialsProvider {

    private final AWSSecurityTokenService tokenService;
    private GetFederationTokenRequest getFederationTokenRequest;
    private GetFederationTokenResult federationToken;

    @Builder
    public FederatedUserCredentialsProvider(AWSCredentialsProvider iamUserCredentialsProvider, String region, String userName, Policy policy) {
        tokenService = AWSSecurityTokenServiceClientBuilder
                .standard()
                .withRegion(region)
                .withCredentials(iamUserCredentialsProvider)
                .build();

        getFederationTokenRequest = new GetFederationTokenRequest()
                .withName(userName)
                .withPolicy(policy.toJson());

        federationToken = tokenService.getFederationToken(getFederationTokenRequest);
    }

    @Override
    public AWSSessionCredentials getCredentials() {
        Credentials credentials = federationToken.getCredentials();

        return new BasicSessionCredentials(credentials.getAccessKeyId(), credentials.getSecretAccessKey(), credentials.getSessionToken());
    }

    @Override
    public void refresh() {
        Instant expirationDate = federationToken.getCredentials().getExpiration().toInstant();
        if (federationToken == null || expirationDate.isAfter(Instant.now())) {
            federationToken = tokenService.getFederationToken(getFederationTokenRequest);
        }
    }
}

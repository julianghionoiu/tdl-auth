package tdl.auth.authorizer;

import io.jsonwebtoken.Claims;

public interface LambdaAuthorizer {

    Claims getClaims(String requestedPrincipal, String officialChallenge, String authToken) throws AuthenticationException, AuthorizationException;
}

package tdl.auth.authorizer;

import io.jsonwebtoken.Claims;

public interface LambdaAuthorizer {

    Claims getClaims(String requestedPrincipal, String authToken) throws AuthenticationException, AuthorizationException;
}

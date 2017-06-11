package tdl.auth.authorizer;

public interface LambdaAuthorizer {
    boolean isAuthorized(String requestedPrincipal, String authToken) throws AuthorizationException;
}

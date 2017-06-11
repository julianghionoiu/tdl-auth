package tdl.auth.authorizer;

public class AuthorizationException extends Exception {
    public AuthorizationException(String message) {
        super(message);
    }
}

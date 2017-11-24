package tdl.auth.authorizer;

public class AuthenticationException extends Exception {
    public AuthenticationException(String message, Exception e) {
        super(message, e);
    }

    @SuppressWarnings("WeakerAccess")
    public AuthenticationException(String message) {
        super(message);
    }
}

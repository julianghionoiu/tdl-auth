package tdl.auth.authorizer;

import ro.ghionoiu.kmsjwt.token.JWTVerificationException;

public class AuthorizationException extends Throwable {
    AuthorizationException(String message, JWTVerificationException e) {
        super(message, e);
    }
}

package tdl.auth.helpers;

class JWTMissingMandatoryKey extends RuntimeException {
    JWTMissingMandatoryKey(String key) {
        super("JWT Missing mandatory key: "+key);
    }
}

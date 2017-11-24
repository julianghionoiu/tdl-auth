package tdl.auth.authorizer;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.services.kms.AWSKMS;
import com.amazonaws.services.kms.AWSKMSClientBuilder;
import io.jsonwebtoken.Claims;
import ro.ghionoiu.kmsjwt.key.KMSDecrypt;
import ro.ghionoiu.kmsjwt.key.KeyDecrypt;
import ro.ghionoiu.kmsjwt.token.JWTDecoder;
import ro.ghionoiu.kmsjwt.token.JWTVerificationException;

import java.util.Collections;
import java.util.Objects;

public class JWTKMSAuthorizer implements LambdaAuthorizer {

    private final JWTDecoder jwtDecoder;

    public JWTKMSAuthorizer(String region, String jwtDecryptKeyARN, AWSCredentialsProvider credential) {
        AWSKMS kmsClient = AWSKMSClientBuilder.standard()
                .withRegion(region)
                .withCredentials(credential)
                .build();
        KMSDecrypt kmsDecrypt = new KMSDecrypt(kmsClient, Collections.singleton(jwtDecryptKeyARN));
        jwtDecoder = new JWTDecoder(kmsDecrypt);
    }

    JWTKMSAuthorizer(KeyDecrypt keyDecrypt) {
        jwtDecoder = new JWTDecoder(keyDecrypt);
    }

    @Override
    public Claims getClaims(String requestedPrincipal, String authToken) throws AuthenticationException, AuthorizationException {
        Claims claims;
        try {
            claims = jwtDecoder.decodeAndVerify(authToken);
            String principalIdFromToken = claims.get("usr", String.class);
            if (!Objects.equals(requestedPrincipal, principalIdFromToken)) {
                throw new AuthorizationException("User not authorized to perform action");
            }

            return claims;
        } catch (JWTVerificationException e) {
            throw new AuthenticationException("JWT Token not valid");
        }
    }
}

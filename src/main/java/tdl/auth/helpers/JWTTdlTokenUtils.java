package tdl.auth.helpers;

import io.jsonwebtoken.Claims;
import ro.ghionoiu.kmsjwt.key.KMSEncrypt;
import ro.ghionoiu.kmsjwt.key.KeyOperationException;
import ro.ghionoiu.kmsjwt.token.JWTEncoder;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class JWTTdlTokenUtils {
    private static final String USERNAME_CLAIM = "usr";
    private static final String CHALLENGES_CLAIM = "tdl_chx";

    public static String generate(KMSEncrypt kmsEncrypt,
                                  String username, List<String> challengeIds,
                                  Date expirationDate) throws KeyOperationException {
        return JWTEncoder.builder(kmsEncrypt)
                .setExpiration(expirationDate)
                .claim(USERNAME_CLAIM, username)
                .claim(CHALLENGES_CLAIM, challengeIds)
                .compact();
    }


    public static List<String> getChallengeIds(Claims claims) {
        if (claims.containsKey(CHALLENGES_CLAIM)) {
            //noinspection unchecked
            return ((List<Object>) claims.get(CHALLENGES_CLAIM, List.class)).stream()
                    .map(Object::toString)
                    .collect(Collectors.toList());
        } else {
            return Collections.emptyList();
        }
    }
}

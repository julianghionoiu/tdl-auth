package tdl.auth.helpers;

import io.jsonwebtoken.Claims;
import ro.ghionoiu.kmsjwt.key.KMSEncrypt;
import ro.ghionoiu.kmsjwt.key.KeyOperationException;
import ro.ghionoiu.kmsjwt.token.JWTEncoder;
import ro.ghionoiu.kmsjwt.token.JWTVerificationException;
import tdl.auth.authorizer.AuthorizationException;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class JWTTdlTokenUtils {
    private static final String USERNAME_CLAIM = "usr";
    private static final String WARMUP_CHALLENGES_CLAIM = "tdl_wrm";
    private static final String OFFICIAL_CHALLENGE_CLAIM = "tdl_chx";

    public static String generate(KMSEncrypt kmsEncrypt,
                                  String username, List<String> warmupChallenges, String officialChallenge,
                                  Date expirationDate) throws KeyOperationException {
        return JWTEncoder.builder(kmsEncrypt)
                .setExpiration(expirationDate)
                .claim(USERNAME_CLAIM, username)
                .claim(WARMUP_CHALLENGES_CLAIM, warmupChallenges)
                .claim(OFFICIAL_CHALLENGE_CLAIM, officialChallenge)
                .compact();
    }

    public static boolean isClaimsMatching(String username, String officialChallenge, Claims claims) {
        String usernameFromClaim = claims.get(USERNAME_CLAIM, String.class);
        String challengeFromClaim = claims.get(OFFICIAL_CHALLENGE_CLAIM, String.class);

        return Objects.equals(username, usernameFromClaim) && Objects.equals(officialChallenge, challengeFromClaim);
    }

    public static List<String> getWarmupChallenges(Claims claims) {
        if (claims.containsKey(WARMUP_CHALLENGES_CLAIM)) {
            //noinspection unchecked
            return ((List<Object>) claims.get(WARMUP_CHALLENGES_CLAIM, List.class)).stream()
                    .map(Object::toString)
                    .collect(Collectors.toList());
        } else {
            return Collections.emptyList();
        }
    }

    public static String getOfficialChallenge(Claims claims) {
        if (claims.containsKey(OFFICIAL_CHALLENGE_CLAIM)) {
            return claims.get(OFFICIAL_CHALLENGE_CLAIM, String.class);
        } else {
            throw new JWTMissingMandatoryKey(OFFICIAL_CHALLENGE_CLAIM);
        }
    }

}

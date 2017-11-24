package tdl.auth.helpers;

import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

public class JourneyIdUtils {

    //Debt: This logic needs to be shared between tdl-auth and tdl-server. At the moment it is duplicated
    public static String encode(String username, List<String> challenges) {
        String challengeCSV = challenges.stream().collect(Collectors.joining(","));
        String unobfuscatedId = username + "|" + challengeCSV + "|" + "Q";
        return Base64.getEncoder().encodeToString(unobfuscatedId.getBytes());
    }
}

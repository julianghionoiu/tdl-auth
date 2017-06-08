package tdl.auth.lambda;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import org.json.JSONObject;

public class CredentialInput {

    public final String username;

    public final String token;

    public CredentialInput(String username, String base64token) {
        this.username = username;
        this.token = decodeToken(base64token);
    }

    private static String decodeToken(String encoded) {
        byte[] decoded = Base64.getDecoder().decode(encoded);
        return new String(decoded, StandardCharsets.UTF_8);
    }

    @Override
    public String toString() {
        return "{" + username + ':' + token + "}";
    }

    public static CredentialInput createFromJsonString(String inputJson) {
        JSONObject json = new JSONObject(inputJson);
        return new CredentialInput(json.getString("username"), json.getString("token"));
    }
}

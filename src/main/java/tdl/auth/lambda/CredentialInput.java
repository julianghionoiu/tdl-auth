package tdl.auth.lambda;

import org.json.JSONObject;

public class CredentialInput {

    public final String username;

    public final String token;

    public CredentialInput(String username, String token) {
        this.username = username;
        this.token = token;
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

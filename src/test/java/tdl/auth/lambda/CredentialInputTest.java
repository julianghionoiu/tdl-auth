package tdl.auth.lambda;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class CredentialInputTest {

    @Test
    public void createFromJsonString() {
        String inputJson = "{\"username\":\"test1\", \"token\":\"token1\"}";
        CredentialInput credentialInput = CredentialInput.createFromJsonString(inputJson);
        assertEquals(credentialInput.username, "test1");
        assertEquals(credentialInput.token, "token1");
        assertEquals(credentialInput.toString(), "{test1:token1}");
    }
}

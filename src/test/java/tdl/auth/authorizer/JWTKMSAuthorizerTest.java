package tdl.auth.authorizer;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import ro.ghionoiu.kmsjwt.key.DummyKeyProtection;
import ro.ghionoiu.kmsjwt.key.KeyOperationException;
import ro.ghionoiu.kmsjwt.token.JWTEncoder;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class JWTKMSAuthorizerTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    private LambdaAuthorizer authorizer;

    @Before
    public void setUp() throws Exception {
        authorizer = new JWTKMSAuthorizer(new DummyKeyProtection());
    }


    @SuppressWarnings("SameParameterValue")
    private String getValidToken(String username) throws KeyOperationException {
        return JWTEncoder.builder(new DummyKeyProtection())
                .claim("usr", username)
                .compact();
    }

    @Test
    public void accepts_valid_token() throws Throwable {
        boolean isAuthorized = authorizer.isAuthorized("test-user", getValidToken("test-user"));
        assertThat(isAuthorized, is(true));
    }

    @Test
    public void rejects_malformed_token() throws Throwable {
        expectedException.expectMessage(containsString("not valid"));
        authorizer.isAuthorized("test-user", "XYZ");
    }

    @Test
    public void rejects_token_that_does_not_match_user() throws Throwable {
        boolean isAuthorized = authorizer.isAuthorized("other-user", getValidToken("test-user"));
        assertThat(isAuthorized, is(false));
    }
}
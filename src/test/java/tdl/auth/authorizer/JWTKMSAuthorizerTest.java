package tdl.auth.authorizer;

import io.jsonwebtoken.Claims;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import ro.ghionoiu.kmsjwt.key.DummyKeyProtection;
import ro.ghionoiu.kmsjwt.key.KeyOperationException;
import ro.ghionoiu.kmsjwt.token.JWTEncoder;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

public class JWTKMSAuthorizerTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    private LambdaAuthorizer authorizer;

    @Before
    public void setUp() {
        authorizer = new JWTKMSAuthorizer(new DummyKeyProtection());
    }


    @SuppressWarnings("SameParameterValue")
    private String getValidToken(String username, String challenge) throws KeyOperationException {
        return JWTEncoder.builder(new DummyKeyProtection())
                .claim("usr", username)
                .claim("tdl_chx", challenge)
                .compact();
    }

    @Test
    public void accepts_valid_token() throws Throwable {
        Claims claims = authorizer.getClaims("test-user",
                "test-challenge",
                getValidToken("test-user", "test-challenge"));
        assertThat(claims, is(notNullValue()));
    }

    @Test
    public void rejects_malformed_token() throws Throwable {
        expectedException.expectMessage(containsString("not valid"));
        authorizer.getClaims("test-user", "test-challenge", "XYZ");
    }

    @Test(expected = AuthorizationException.class)
    public void rejects_token_that_does_not_match_user() throws Throwable {
        authorizer.getClaims("other-user", "test-challenge",
                getValidToken("test-user", "test-challenge"));
    }

    @Test(expected = AuthorizationException.class)
    public void rejects_token_that_does_not_match_challenge() throws Throwable {
        authorizer.getClaims("test-user", "other-challenge",
                getValidToken("test-user", "test-challenge"));
    }
}
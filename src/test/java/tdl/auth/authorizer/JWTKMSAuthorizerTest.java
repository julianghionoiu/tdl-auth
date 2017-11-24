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
        Claims claims = authorizer.getClaims("test-user", getValidToken("test-user"));
        assertThat(claims, is(notNullValue()));
    }

    @Test
    public void rejects_malformed_token() throws Throwable {
        expectedException.expectMessage(containsString("not valid"));
        authorizer.getClaims("test-user", "XYZ");
    }

    @Test(expected = AuthorizationException.class)
    public void rejects_token_that_does_not_match_user() throws Throwable {
        authorizer.getClaims("other-user", getValidToken("test-user"));
    }
}
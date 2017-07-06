package tdl.auth.linkgenerator;

import freemarker.template.TemplateException;
import java.io.IOException;
import java.util.Date;

import org.junit.Test;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

public class IntroPageTemplateTest {

    @Test
    public void generate() throws IOException, TemplateException {
        String challengeTitle = "challengeTitle";
        String sponsorName = "sponsorName";
        String username = "myUsername";
        String token = "myToken";
        String sessionId = "mySessionId";
        String url = "https://www.example.com/production/verify";
        IntroPageTemplate introPageTemplate = new IntroPageTemplate("test-intro.html.ftl");
        String content = introPageTemplate.generateContent(
                challengeTitle,
                sponsorName,
                username,
                token,
                sessionId,
                url,
                new Date(),
                sessionId);
        assertThat(content, containsString(challengeTitle));
        assertThat(content, containsString(sponsorName));
        assertThat(content, containsString(username));
        assertThat(content, containsString(token));
        assertThat(content, containsString(url));
        assertThat(content, containsString(sessionId));
    }

}

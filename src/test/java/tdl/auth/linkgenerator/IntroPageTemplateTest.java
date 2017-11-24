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
        String codingSessionDurationLabel = "3hours";
        String token = "myToken";
        String journeyId = "myJourneyId";
        String url = "https://www.example.com/production/verify";
        IntroPageTemplate introPageTemplate = new IntroPageTemplate("test-intro.html.ftl");
        String content = introPageTemplate.generateContent(
                challengeTitle,
                sponsorName,
                codingSessionDurationLabel,
                username,
                token,
                url,
                new Date(),
                journeyId);
        assertThat(content, containsString(challengeTitle));
        assertThat(content, containsString(sponsorName));
        assertThat(content, containsString(username));
        assertThat(content, containsString(codingSessionDurationLabel));
        assertThat(content, containsString(token));
        assertThat(content, containsString(url));
        assertThat(content, containsString(journeyId));
    }

}

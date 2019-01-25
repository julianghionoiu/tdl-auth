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
        String resourcesWebPath = "webPath";
        String headerImageName = "headerImageName.jpg";
        String challengeTitle = "challengeTitle";
        String sponsorName = "sponsorName";
        String username = "myUsername";
        String challenge = "myChallenge";
        String codingSessionDurationLabel = "3hours";
        String token = "myToken";
        String journeyId = "myJourneyId";
        String authUrl = "https://www.example.com/production/verify";
        IntroPageTemplate introPageTemplate = new IntroPageTemplate(
                "test-intro.html.ftl",
                resourcesWebPath,
                authUrl);
        String content = introPageTemplate.generateContent(
                headerImageName,
                challengeTitle,
                sponsorName,
                codingSessionDurationLabel,
                true,
                username,
                challenge,
                token,
                new Date(),
                journeyId);
        assertThat(content, containsString(resourcesWebPath+"/"+headerImageName));
        assertThat(content, containsString(challengeTitle));
        assertThat(content, containsString(sponsorName));
        assertThat(content, containsString(username));
        assertThat(content, containsString(challenge));
        assertThat(content, containsString(codingSessionDurationLabel));
        assertThat(content, containsString("no video option enabled"));
        assertThat(content, containsString(token));
        assertThat(content, containsString(authUrl));
        assertThat(content, containsString(journeyId));
    }

}

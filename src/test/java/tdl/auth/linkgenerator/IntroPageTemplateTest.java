package tdl.auth.linkgenerator;

import freemarker.template.TemplateException;
import org.junit.Test;

import java.io.IOException;
import java.util.Date;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertThat;

public class IntroPageTemplateTest {

    @Test
    public void generate() throws IOException, TemplateException {
        String resourcesWebPath = "webPath";
        String authUrl = "https://www.example.com/production/verify";
        IntroPageTemplate introPageTemplate = new IntroPageTemplate(
                "test-intro.html.ftl",
                resourcesWebPath,
                authUrl);
        IntroPageParameters params = IntroPageParameters.builder()
                .headerImageName("headerImageName.jpg")
                .mainChallengeTitle("challengeTitle")
                .sponsorName("sponsorName")
                .codingSessionDurationLabel("3hours")
                .enableNoVideoOption(true)
                .enableApplyPressure(true)
                .username("myUsername")
                .challenge("myChallenge")
                .token("myToken")
                .expirationDate(new Date())
                .journeyId("myJourneyId")
                .build();
        String content = introPageTemplate.generateContent(params);
        assertThat(content, containsString(resourcesWebPath+"/"+ params.getHeaderImageName()));
        assertThat(content, containsString(params.getMainChallengeTitle()));
        assertThat(content, containsString(params.getSponsorName()));
        assertThat(content, containsString(params.getUsername()));
        assertThat(content, containsString(params.getChallenge()));
        assertThat(content, containsString(params.getCodingSessionDurationLabel()));
        assertThat(content, containsString("no video option enabled"));
        assertThat(content, containsString("applying pressure"));
        assertThat(content, containsString(params.getToken()));
        assertThat(content, containsString(authUrl));
        assertThat(content, containsString(params.getJourneyId()));
    }

}

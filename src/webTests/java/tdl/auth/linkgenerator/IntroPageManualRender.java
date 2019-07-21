package tdl.auth.linkgenerator;

import freemarker.template.TemplateException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Optional;


public class IntroPageManualRender {

    //DEBT This can be transformed into a visual approval test
    /**
     * Use this to visually inspect the rendering of the candidate page
     */
    public static void main(String[] args) throws IOException, TemplateException, ParseException {
        IntroPageTemplate template = new IntroPageTemplate(
                "intro.html.ftl",
                "../staticResources",
                "https://www.example.com/production/verify");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        IntroPageParameters introPageParameters = IntroPageParameters.builder()
                .headerImageName("recworks.png")
                .mainChallengeTitle("Developer Insights")
                .sponsorName("RecWorks")
                .enableApplyPressure(false)
                .videoRecordingOption(VideoRecordingOption.MANDATORY)
                .inspiredByLabel("real business domain")
                .codingSessionDurationLabel("3 hours")
                .defaultLanguage("Java")
                .username("xwya01")
                .challenge("CHK")
                .token("asdf")
                .expirationDate(simpleDateFormat.parse("31/02/2019"))
                .fakeCurrentDate(Optional.of(simpleDateFormat.parse("30/02/2019")))
                .journeyId("myJourneyId")
                .build();
        String content = template.generateContent(introPageParameters);
        Files.write(Paths.get("./build/intro.html"), content.getBytes(),
                StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING);
    }

}

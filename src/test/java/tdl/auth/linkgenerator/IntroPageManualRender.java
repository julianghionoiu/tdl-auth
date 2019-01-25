package tdl.auth.linkgenerator;

import freemarker.template.TemplateException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Date;


public class IntroPageManualRender {

    //DEBT This can be transformed into a visual approval test
    /**
     * Use this to visually inspect the rendering of the candidate page
     */
    public static void main(String[] args) throws IOException, TemplateException {
        IntroPageTemplate template = new IntroPageTemplate(
                "intro.html.ftl",
                "../000common", "https://www.example.com/production/verify");
        String content = template.generateContent(
                "headerImageName.jpg",
                "challengeTitle",
                "sponsorName",
                "3hours",
                true,
                "myUsername",
                "myChallenge",
                "myToken",
                new Date(),
                "myJourneyId");
        String path = "./build/intro.html";
        Files.write( Paths.get(path), content.getBytes(), StandardOpenOption.CREATE);
    }

}

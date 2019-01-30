package tdl.auth.linkgenerator;

import freemarker.template.TemplateException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
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
                "../staticResources",
                "https://www.example.com/production/verify");
        String content = template.generateContent(
                "accelerate.jpg",
                "Developer Insights",
                "Valtech",
                "3 hours",
                true,
                "xwya01",
                "CHK",
                "asgahdfh",
                new Date(),
                "myJourneyId");
        Files.write(Paths.get("./build/intro.html"), content.getBytes(),
                StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING);
    }

}

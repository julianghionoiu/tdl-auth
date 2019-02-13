package tdl.auth.linkgenerator;

import freemarker.template.TemplateException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.ParseException;
import java.text.SimpleDateFormat;


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
        String content = template.generateContent(
                "makers.jpg",
                "Developer Insights",
                "Makers Academy",
                "3 hours",
                true,
                "xwya01",
                "CHK",
                "asgahdfh",
                new SimpleDateFormat("dd/MM/yyyy").parse("31/02/2020"),
                "myJourneyId");
        Files.write(Paths.get("./build/intro.html"), content.getBytes(),
                StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING);
    }

}

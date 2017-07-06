package tdl.auth.linkgenerator;

import freemarker.template.TemplateException;
import java.io.IOException;
import org.junit.Test;
import tdl.auth.LinkGeneratorLambdaHandler;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

public class IntroPageTemplateTest {

    @Test
    public void generate() throws IOException, TemplateException {
        String username = "myUsername";
        String token = "myToken";
        String sessionId = "mySessionId";
        String url = "https://www.example.com/production/verify";
        IntroPageTemplate introPageTemplate = new IntroPageTemplate("test-intro.html.ftl");
        String content = introPageTemplate.generateContent(username, token, sessionId, url);
        assertThat(content, containsString(username));
        assertThat(content, containsString(token));
        assertThat(content, containsString(url));
        //System.out.println(content);
    }

}

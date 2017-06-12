package tdl.auth.linkgenerator;

import freemarker.template.TemplateException;
import java.io.IOException;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import tdl.auth.LinkGeneratorLambdaHandler;

public class PageTest {

    @Test
    public void testGenerate() throws IOException, TemplateException {
        String username = "username";
        String token = "token";
        String url = "https://www.example.com/production/verify";
        Page page = new Page(username, token, url);
        page.setTemplateConfiguration(LinkGeneratorLambdaHandler.templateConfiguration);
        String content = page.generateContent();
        assertThat(content, containsString(username));
        assertThat(content, containsString(token));
        assertThat(content, containsString(url));
        //System.out.println(content);
    }
}

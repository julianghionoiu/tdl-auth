package tdl.auth.linkgenerator;

import freemarker.template.TemplateException;
import java.io.IOException;
import org.junit.Test;
import tdl.auth.LinkGeneratorLambdaHandler;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

public class PageTest {

    @Test
    public void generate() throws IOException, TemplateException {
        String username = "myUsername";
        String token = "myToken";
        String sessionId = "mySessionId";
        String url = "https://www.example.com/production/verify";
        Page page = new Page(username, token, sessionId, url, LinkGeneratorLambdaHandler.templateConfiguration);
        String content = page.getContent();
        assertThat(content, containsString(username));
        assertThat(content, containsString(token));
        assertThat(content, containsString(url));
        //System.out.println(content);
    }

}

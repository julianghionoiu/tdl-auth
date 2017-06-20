package tdl.auth.linkgenerator;

import com.amazonaws.services.s3.AmazonS3;
import freemarker.template.TemplateException;
import java.io.IOException;
import org.junit.Test;
import tdl.auth.LinkGeneratorLambdaHandler;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import static org.mockito.Mockito.*;

public class PageTest {

    @Test
    public void generate() throws IOException, TemplateException {
        String username = "username";
        String token = "token";
        String url = "https://www.example.com/production/verify";
        Page page = new Page(username, token, url, LinkGeneratorLambdaHandler.templateConfiguration);
        String content = page.getContent();
        assertThat(content, containsString(username));
        assertThat(content, containsString(token));
        assertThat(content, containsString(url));
        //System.out.println(content);
    }

}

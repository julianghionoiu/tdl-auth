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
        Page page = new Page(username, token, url);
        page.setTemplateConfiguration(LinkGeneratorLambdaHandler.templateConfiguration);
        String content = page.generateContent();
        assertThat(content, containsString(username));
        assertThat(content, containsString(token));
        assertThat(content, containsString(url));
        //System.out.println(content);
    }

    @Test
    public void getPublicUrl() {

    }

    @Test
    public void generateKey() {
        String username = "username";
        String token = "token";
        String url = "https://www.example.com/production/verify";
        Page page = new Page(username, token, url);
        String key = page.generateDirectory();
        assertEquals(key.length(), Page.KEY_LENGTH);
    }

    @Test
    public void createClient() throws IOException, TemplateException {
        Page page = mock(Page.class);
        doCallRealMethod().when(page).createClient();
        Object client = page.createClient();
        assertThat(client, instanceOf(AmazonS3.class));
    }
}

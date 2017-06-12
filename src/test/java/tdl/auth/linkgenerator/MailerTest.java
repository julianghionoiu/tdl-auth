package tdl.auth.linkgenerator;

import freemarker.template.TemplateException;
import java.io.IOException;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import tdl.auth.LinkGeneratorLambdaHandler;

public class MailerTest {

    @Test
    public void generateBody() throws IOException, TemplateException {
        String email = "email@example.com";
        String pageUrl = "https://www.example.com/testing";
        Mailer mailer = new Mailer(email, pageUrl);
        mailer.setTemplateConfiguration(LinkGeneratorLambdaHandler.templateConfiguration);
        String content = mailer.createBody();
        assertThat(content, containsString(pageUrl));
        //System.out.println(content);
    }
}

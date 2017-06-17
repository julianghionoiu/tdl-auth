package tdl.auth.linkgenerator;

import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.model.SendEmailResult;
import freemarker.template.TemplateException;
import java.io.IOException;
import org.junit.Test;
import tdl.auth.LinkGeneratorLambdaHandler;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import static org.mockito.Mockito.*;

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

    @Test
    public void send() throws IOException, TemplateException {
        Mailer mailer = mock(Mailer.class);
        mailer.setTemplateConfiguration(LinkGeneratorLambdaHandler.templateConfiguration);
        doReturn("Content").when(mailer).createBody();
        doCallRealMethod().when(mailer).send();

        AmazonSimpleEmailService client = mock(AmazonSimpleEmailService.class);
        doReturn(mock(SendEmailResult.class)).when(client).sendEmail(any());
        doReturn(client).when(mailer).createClient();

        mailer.send();
        verify(client, times(1)).sendEmail(any());
    }

    @Test
    public void createClient() throws IOException, TemplateException {
        Mailer mailer = mock(Mailer.class);
        doCallRealMethod().when(mailer).createClient();
        Object client = mailer.createClient();
        assertThat(client, instanceOf(AmazonSimpleEmailService.class));
    }
    
    @Test
    public void getSender() {
        Mailer mailer = mock(Mailer.class);
        doCallRealMethod().when(mailer).getSender();
        assertEquals(mailer.getSender(), System.getenv("SENDER"));
    }
}

package tdl.auth;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import freemarker.template.TemplateException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import org.junit.Test;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import tdl.auth.linkgenerator.Mailer;

public class MailLinkLambdaHandlerTest {
    
    @Test
    public void createMailer() {
        MailLinkLambdaHandler handler = spy(new MailLinkLambdaHandler());
        Object mailer = handler.createMailer("test@email.com", "http://www.example.com");
        assertThat(mailer, instanceOf(Mailer.class));
    }

    @Test
    public void handleRequestShouldReturnOkay() throws IOException, TemplateException {
        Map<String, Object> input = new HashMap<>();
        input.put("email", "test@email.com");
        input.put("url", "http://www.example.com");

        MailLinkLambdaHandler handler = spy(new MailLinkLambdaHandler());
        Mailer mailer = mock(Mailer.class);
        doNothing().when(mailer).send();
        doReturn(mailer).when(handler).createMailer(anyString(), anyString());
        Context context = mock(Context.class);
        
        String result = handler.handleRequest(input, context);
        assertEquals(result, "OK");
    }
    
    @Test
    public void handleRequestShouldReturnNotOkayOnException() throws IOException, TemplateException {
        Map<String, Object> input = new HashMap<>();
        input.put("email", "test@email.com");
        input.put("url", "http://www.example.com");

        MailLinkLambdaHandler handler = spy(new MailLinkLambdaHandler());
        Mailer mailer = mock(Mailer.class);
        doThrow(new IOException()).when(mailer).send();
        doReturn(mailer).when(handler).createMailer(anyString(), anyString());
        Context context = mock(Context.class);
        doReturn(mock(LambdaLogger.class)).when(context).getLogger();
        
        String result = handler.handleRequest(input, context);
        assertEquals(result, "NOT OK");
    }
}

package tdl.auth;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import java.util.HashMap;
import java.util.Map;
import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.EnvironmentVariables;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

//Email address is not verified.
public class MailLinkLambdaHandlerAcceptanceTest {
//    
//    @Rule
//    public EnvironmentVariables environmentVariables = new EnvironmentVariables();
//
//    @Test
//    public void send_email() {
//        environmentVariables.set("SENDER", "sender@test.example.com");
//        MailLinkLambdaHandler handler = new MailLinkLambdaHandler();
//        Map<String, Object> input = new HashMap<>();
//        input.put("email", "success@simulator.amazonses.com");
//        input.put("url", "http://www.example.com/production/verify");
//
//        handler.handleRequest(input, createTestContext());
//    }
//
//    private Context createTestContext() {
//        Context context = mock(Context.class);
//        doReturn(mock(LambdaLogger.class)).when(context).getLogger();
//        return context;
//    }
}

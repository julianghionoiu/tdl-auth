package tdl.auth;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import freemarker.template.TemplateException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import ro.ghionoiu.kmsjwt.key.KeyOperationException;
import tdl.auth.linkgenerator.LinkGeneratorRequest;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class LinkGeneratorLambdaTest {
    
    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void getLambdaExceptionTypeLabel() {
        Exception ex1 = new IOException("Message");
        String label1 = LinkGeneratorLambdaHandler.getLambdaExceptionTypeLabel(ex1);
        assertEquals(label1, "UnknownException");
        
        Exception ex2 = new RuntimeException("Message");
        String label2 = LinkGeneratorLambdaHandler.getLambdaExceptionTypeLabel(ex2);
        assertEquals(label2, "UnknownException");
        
        Exception ex3 = new TemplateException("Message", null);
        String label3 = LinkGeneratorLambdaHandler.getLambdaExceptionTypeLabel(ex3);
        assertEquals(label3, "Email");
    }
    
    @Test
    public void handleRequestShouldThrowException() throws TemplateException, KeyOperationException, IOException {
        expectedException.expect(RuntimeException.class);
        expectedException.expectMessage("Email");
        LinkGeneratorLambdaHandler handler = mock(LinkGeneratorLambdaHandler.class);
        doCallRealMethod().when(handler).handleRequest(any(), any());
        Exception ex = new TemplateException("Message", null);
        doThrow(ex).when(handler).getUploadPageUrlFromRequest(any(), any());
        
        Context context = mock(Context.class);
        LambdaLogger logger = mock(LambdaLogger.class);
        doNothing().when(logger).log(anyString());
        doReturn(logger).when(context).getLogger();
        
        handler.handleRequest(mock(LinkGeneratorRequest.class), context);
    }
}

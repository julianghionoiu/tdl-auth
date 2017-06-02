package tdl.auth;

import com.amazonaws.services.lambda.runtime.Context;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import org.json.JSONObject;

import org.junit.Test;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class LambdaHandlerTest {

    @Test
    public void testHandler() throws IOException {
        Context context = mock(Context.class);
        HashMap<String, String> input = new HashMap<>();
        input.put("data", "SGVsbG8gV29ybGQh");
        JSONObject json = new JSONObject(input);

        InputStream inputStream = new ByteArrayInputStream(json.toString().getBytes(StandardCharsets.UTF_8));
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        LambdaHandler handler = new LambdaHandler();

        handler.handleRequest(inputStream, outputStream, context);

        assertEquals(outputStream.toString(), "Hello World!");
    }
}

package tdl.auth;

import com.amazonaws.services.lambda.runtime.Context;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Optional;

import org.json.JSONObject;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.containsString;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class LambdaHandlerTest {
    private static final String TEST_AWS_REGION = Optional.ofNullable(System.getenv("TEST_AWS_REGION"))
            .orElse("eu-west-2");
    private static final String TEST_DESTINATION_BUCKET = Optional.ofNullable(System.getenv("TEST_DESTINATION_BUCKET"))
            .orElse("tdl-test-auth");


    @Test
    public void testHandler() throws IOException {
        Context context = mock(Context.class);
        when(context.getLogger()).thenReturn(System.out::println);
        
        HashMap<String, Object> input = new HashMap<>();
        input.put("username", "tdl-test-user01");
        input.put("token", "SGVsbG8gV29ybGQh");
        JSONObject json = new JSONObject(input);

        InputStream inputStream = new ByteArrayInputStream(json.toString().getBytes(StandardCharsets.UTF_8));
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        LambdaHandler handler = new LambdaHandler(TEST_DESTINATION_BUCKET, TEST_AWS_REGION);

        handler.handleRequest(inputStream, outputStream, context);

        assertThat(outputStream.toString(), containsString("s3_prefix=tdl-test-user01"));
    }
}

package tdl.auth;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.stream.Collectors;
import org.json.JSONObject;

public class LambdaHandler implements RequestStreamHandler {

    @Override
    public void handleRequest(InputStream inputStream, OutputStream outputStream, Context context) throws IOException {
        String inputJson = getStringInput(inputStream);
        String base64data = getDataFromJson(inputJson);
        String data = getDecodedValue(base64data);
        outputStream.write(data.getBytes(StandardCharsets.UTF_8));
    }

    private static String getStringInput(InputStream inputStream) {
        return new BufferedReader(new InputStreamReader(inputStream))
                .lines().collect(Collectors.joining(""));
    }

    private static String getDataFromJson(String inputJson) {
        JSONObject json = new JSONObject(inputJson);
        return json.getString("data");
    }

    private static String getDecodedValue(String encoded) {
        byte[] decoded = Base64.getDecoder().decode(encoded);
        return new String(decoded, StandardCharsets.UTF_8);
    }
}

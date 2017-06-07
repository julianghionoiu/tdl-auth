package tdl.auth;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import org.json.JSONObject;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.stream.Collectors;

public class LambdaHandler implements RequestStreamHandler {

    @Override
    public void handleRequest(InputStream inputStream, OutputStream outputStream, Context context) throws IOException {
        try {
            String inputJson = getStringInput(inputStream);
            context.getLogger().log("inputJson:"+inputJson);
            String base64data = getDataFromJson(inputJson);
            context.getLogger().log("base64data:"+base64data);
            String decodedValue = getDecodedValue(base64data);
            context.getLogger().log("decodedValue:"+decodedValue);
            outputStream.write(decodedValue.getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            e.printStackTrace();
        }
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

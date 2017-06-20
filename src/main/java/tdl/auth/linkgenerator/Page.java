package tdl.auth.linkgenerator;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Page {

    static final long KEY_LENGTH = 32;

    private final String username;

    private final String token;

    private final String apiEndpointUrl;

    private final String content;

    private Configuration templateConfiguration;

    public Page(String username, String token, String apiEndpointUrl, Configuration templateConfiguration) throws IOException, TemplateException {
        this.username = username;
        this.token = token;
        this.apiEndpointUrl = apiEndpointUrl;
        this.templateConfiguration = templateConfiguration;
        content = generateContent();
    }

    private final String generateContent() throws IOException, TemplateException {
        Template template = templateConfiguration.getTemplate("page.html");
        StringWriter stringWriter = new StringWriter();
        Map<String, String> contentParams = new HashMap<>();
        contentParams.put("API_ENDPOINT", apiEndpointUrl);
        contentParams.put("USERNAME", username);
        contentParams.put("TOKEN", token);
        template.process(contentParams, stringWriter);
        return stringWriter.toString();
    }

    public String getContent() {
        return content;
    }
}

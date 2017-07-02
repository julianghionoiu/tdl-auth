package tdl.auth.linkgenerator;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

public class Page {

    static final long KEY_LENGTH = 32;

    private final String username;

    private final String token;

    private final String sessionId;

    private final String authVerifyEndpointUrl;

    private final String content;

    private Configuration templateConfiguration;

    public Page(String username, String token, String sessionId, String authVerifyEndpointUrl, Configuration templateConfiguration) throws IOException, TemplateException {
        this.username = username;
        this.token = token;
        this.sessionId = sessionId;
        this.authVerifyEndpointUrl = authVerifyEndpointUrl;
        this.templateConfiguration = templateConfiguration;
        content = generateContent();
    }

    private String generateContent() throws IOException, TemplateException {
        Template template = templateConfiguration.getTemplate("page.html");
        StringWriter stringWriter = new StringWriter();
        Map<String, String> contentParams = new HashMap<>();
        contentParams.put("API_VERIFY_ENDPOINT", authVerifyEndpointUrl);
        contentParams.put("USERNAME", username);
        contentParams.put("TOKEN", token);
        contentParams.put("SESSION_ID", sessionId);
        template.process(contentParams, stringWriter);
        return stringWriter.toString();
    }

    String getContent() {
        return content;
    }
}

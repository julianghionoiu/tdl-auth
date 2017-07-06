package tdl.auth.linkgenerator;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import tdl.auth.LinkGeneratorLambdaHandler;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

public class IntroPageTemplate {

    private Template template;


    public IntroPageTemplate(String templateName) throws IOException {
        Configuration configuration = new Configuration();
        configuration.setClassForTemplateLoading(IntroPageTemplate.class, "/templates/");
        template = configuration.getTemplate(templateName);
    }

    public String generateContent(String username, String token, String sessionId, String authVerifyEndpointUrl)
            throws IOException, TemplateException {
        StringWriter stringWriter = new StringWriter();
        Map<String, String> contentParams = new HashMap<>();
        contentParams.put("API_VERIFY_ENDPOINT", authVerifyEndpointUrl);
        contentParams.put("USERNAME", username);
        contentParams.put("TOKEN", token);
        contentParams.put("SESSION_ID", sessionId);
        contentParams.put("CHALLENGE_NAME", "X");
        contentParams.put("SPONSOR", "Y");
        template.process(contentParams, stringWriter);
        return stringWriter.toString();
    }
}

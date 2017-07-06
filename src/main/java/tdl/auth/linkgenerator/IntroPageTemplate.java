package tdl.auth.linkgenerator;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.IOException;
import java.io.StringWriter;
import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class IntroPageTemplate {

    private final Template template;
    private final DateFormat dateFormatter;


    public IntroPageTemplate(String templateName) throws IOException {
        Configuration configuration = new Configuration();
        configuration.setClassForTemplateLoading(IntroPageTemplate.class, "/templates/");
        template = configuration.getTemplate(templateName);
        dateFormatter = DateFormat.getDateInstance(DateFormat.FULL);
    }

    public String generateContent(String mainChallengeTitle,
                                  String sponsorName,
                                  String codingSessionDurationLabel, String username,
                                  String token,
                                  String authVerifyEndpointUrl,
                                  Date expirationDate,
                                  String sessionId)
            throws IOException, TemplateException {
        StringWriter stringWriter = new StringWriter();
        Map<String, String> contentParams = new HashMap<>();
        contentParams.put("MAIN_CHALLENGE_TITLE", mainChallengeTitle);
        contentParams.put("SPONSOR", sponsorName);
        contentParams.put("EXPIRATION_DATE", dateFormatter.format(expirationDate));
        contentParams.put("CODING_SESSION_DURATION", codingSessionDurationLabel);
        contentParams.put("API_VERIFY_ENDPOINT", authVerifyEndpointUrl);
        contentParams.put("USERNAME", username);
        contentParams.put("TOKEN", token);
        contentParams.put("SESSION_ID", sessionId);
        template.process(contentParams, stringWriter);
        return stringWriter.toString();
    }
}

package tdl.auth.linkgenerator;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class IntroPageTemplate {

    private final Template template;

    private final String resourcesWebPath;
    private final String authVerifyEndpointUrl;


    public IntroPageTemplate(String templateName,
                             String resourcesWebPath,
                             String authVerifyEndpointUrl) throws IOException {
        Configuration configuration = new Configuration();
        configuration.setClassForTemplateLoading(IntroPageTemplate.class, "/templates/");

        template = configuration.getTemplate(templateName);

        this.resourcesWebPath = resourcesWebPath;
        this.authVerifyEndpointUrl = authVerifyEndpointUrl;
    }

    public String generateContent(String headerImageName,
                                  String mainChallengeTitle,
                                  String sponsorName,
                                  String codingSessionDurationLabel,
                                  Boolean allowNoVideoOption,
                                  String username,
                                  String challenge,
                                  String token,
                                  Date expirationDate,
                                  String journeyId)
            throws IOException, TemplateException {
        StringWriter stringWriter = new StringWriter();
        Map<String, Object> contentParams = new HashMap<>();
        contentParams.put("RESOURCES_WEB_PATH", resourcesWebPath);
        contentParams.put("HEADER_IMAGE_NAME", headerImageName);
        contentParams.put("MAIN_CHALLENGE_TITLE", mainChallengeTitle);
        contentParams.put("SPONSOR", sponsorName);
        contentParams.put("EXPIRATION_DATE", toSeconds(expirationDate.getTime()));
        contentParams.put("CODING_SESSION_DURATION", codingSessionDurationLabel);
        contentParams.put("ALLOW_NO_VIDEO_OPTION", allowNoVideoOption);
        contentParams.put("API_VERIFY_ENDPOINT", authVerifyEndpointUrl);
        contentParams.put("USERNAME", username);
        contentParams.put("CHALLENGE", challenge);
        contentParams.put("TOKEN", token);
        contentParams.put("JOURNEY_ID", journeyId);
        template.process(contentParams, stringWriter);
        return stringWriter.toString();
    }

    private long toSeconds(long time) {
        return time/1000;
    }
}

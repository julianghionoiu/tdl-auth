package tdl.auth.linkgenerator;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.IOException;
import java.io.StringWriter;
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

    public String generateContent(IntroPageParameters introPageParameters)
            throws IOException, TemplateException {
        StringWriter stringWriter = new StringWriter();
        Map<String, Object> contentParams = new HashMap<>();
        contentParams.put("RESOURCES_WEB_PATH", resourcesWebPath);
        contentParams.put("API_VERIFY_ENDPOINT", authVerifyEndpointUrl);
        contentParams.put("HEADER_IMAGE_NAME", introPageParameters.getHeaderImageName());
        contentParams.put("MAIN_CHALLENGE_TITLE", introPageParameters.getMainChallengeTitle());
        contentParams.put("SPONSOR", introPageParameters.getSponsorName());
        contentParams.put("INSPIRED_BY", introPageParameters.getInspiredByLabel());
        contentParams.put("EXPIRATION_DATE", introPageParameters.getExpirationDate().getTime());
        contentParams.put("CODING_SESSION_DURATION", introPageParameters.getCodingSessionDurationLabel());
        contentParams.put("DEFAULT_LANGUAGE", introPageParameters.getDefaultLanguage());
        contentParams.put("VIDEO_RECORDING_OPTION", introPageParameters.getVideoRecordingOption());
        contentParams.put("ENABLE_APPLY_PRESSURE", introPageParameters.getEnableApplyPressure());
        contentParams.put("ENABLE_REPORT_SHARING", introPageParameters.getEnableReportSharing());
        contentParams.put("USERNAME", introPageParameters.getUsername());
        contentParams.put("CHALLENGE", introPageParameters.getChallenge());
        contentParams.put("TOKEN", introPageParameters.getToken());
        contentParams.put("JOURNEY_ID", introPageParameters.getJourneyId());

        // Lock time during testing
        introPageParameters.getFakeCurrentDate()
                .ifPresent(date -> contentParams.put("FAKE_CURRENT_DATE", date.getTime()));

        template.process(contentParams, stringWriter);
        return stringWriter.toString();
    }

    private long toSeconds(long time) {
        return time/1000;
    }
}

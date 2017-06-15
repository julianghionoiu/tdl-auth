package tdl.auth;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import tdl.auth.helpers.LambdaExceptionLogger;
import tdl.auth.linkgenerator.Mailer;

import java.io.IOException;
import java.util.Map;

/**
 * This handler receives JSON containing email, username, challengeId, validity.
 */
public class MailLinkLambdaHandler implements RequestHandler<Map<String, Object>, String> {

    private static Configuration templateConfiguration;

    static {
        templateConfiguration = createDefaultTemplateConfiguration();
    }

    private static Configuration createDefaultTemplateConfiguration() {
        Configuration configuration = new Configuration();
        configuration.setClassForTemplateLoading(MailLinkLambdaHandler.class, "/templates/");
        return configuration;
    }

    @Override
    public String handleRequest(Map<String, Object> request, Context context) {
        try {
            String email = request.get("email").toString();
            String url = request.get("url").toString();
            Mailer mailer = new Mailer(email, url);
            mailer.setTemplateConfiguration(templateConfiguration);
            mailer.send();
            return "OK";
        } catch (IOException | TemplateException ex) {
            LambdaExceptionLogger.logException(context, ex);
            return "NOT OK"; //TODO: Fix this
        }
    }
}

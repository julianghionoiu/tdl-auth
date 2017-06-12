package tdl.auth;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import tdl.auth.linkgenerator.Mailer;
import tdl.auth.linkgenerator.Page;

public class LinkGeneratorLambdaHandler implements RequestHandler<String, String> {

    public static Configuration templateConfiguration;

    static {
        templateConfiguration = createDefaultTemplateConfiguration();
    }

    private static Configuration createDefaultTemplateConfiguration() {
        Configuration configuration = new Configuration();
        configuration.setClassForTemplateLoading(LinkGeneratorLambdaHandler.class, "/templates/");
        return configuration;
    }

    @Override
    public String handleRequest(String email, Context cntxt) {
        try {
            Page page = new Page(getUsername(), getToken(), getApiEndpointUrl());
            page.generateAndUpload();
            Mailer mailer = new Mailer(email, page.getPublicUrl());
            mailer.send();
            return "OK";
        } catch (IOException | TemplateException ex) {
            Logger.getLogger(LinkGeneratorLambdaHandler.class.getName()).log(Level.SEVERE, null, ex);
            return "NOT OK"; //TODO: Fix this
        }
    }

    private String getApiEndpointUrl() {
        return System.getenv("API_ENDPOINT_URL");
    }

    private String getUsername() {
        return "";
    }

    private String getToken() {
        return "";
    }
}

package tdl.auth.linkgenerator;

import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClient;
import com.amazonaws.services.simpleemail.model.Body;
import com.amazonaws.services.simpleemail.model.Content;
import com.amazonaws.services.simpleemail.model.Destination;
import com.amazonaws.services.simpleemail.model.Message;
import com.amazonaws.services.simpleemail.model.SendEmailRequest;
import com.amazonaws.services.simpleemail.model.SendEmailResult;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

public class Mailer {

    public static final String SUBJECT = "Subject";

    private final String email;

    private final String pageUrl;

    private AmazonSimpleEmailService client;

    private Configuration templateConfiguration;

    public Mailer(String email, String pageUrl) {
        this.email = email;
        this.pageUrl = pageUrl;
    }

    public void setTemplateConfiguration(Configuration templateConfiguration) {
        this.templateConfiguration = templateConfiguration;
    }

    public String createBody() throws IOException, TemplateException {
        Template template = templateConfiguration.getTemplate("mail.txt");
        StringWriter stringWriter = new StringWriter();
        Map<String, String> contentParams = new HashMap<>();
        contentParams.put("PAGE_URL", pageUrl);
        template.process(contentParams, stringWriter);
        return stringWriter.toString();
    }

    public String getSender() {
        return System.getenv("SENDER");
    }

    public void send() throws IOException, TemplateException {
        client = createClient();
        Destination destination = new Destination().withToAddresses(new String[]{email});
        Content subject = new Content().withData(SUBJECT);
        String bodyContent = createBody();
        Content textContent = new Content().withData(bodyContent);
        Body body = new Body().withText(textContent);
        Message message = new Message()
                .withSubject(subject)
                .withBody(body);
        SendEmailRequest request = new SendEmailRequest()
                .withSource(getSender())
                .withDestination(destination)
                .withMessage(message);

        client.sendEmail(request);
    }

    public AmazonSimpleEmailService createClient() {
        return AmazonSimpleEmailServiceClient
                .builder()
                .withRegion(Regions.US_EAST_1)
                .build();
    }

}

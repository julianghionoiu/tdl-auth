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
import java.net.URL;

public final class Mailer {

    public static final String FROM = "sender@example.com";

    public static final String SUBJECT = "Subject";

    private final String email;

    private final URL pageUrl;

    private final AmazonSimpleEmailService client;

    public Mailer(String email, URL pageUrl) {
        this.email = email;
        this.pageUrl = pageUrl;
        this.client = createClient();
    }

    public String createBody() {
        return ""; //TODO: Create HTML in resources
    }

    public void send() {
        Destination destination = new Destination().withToAddresses(new String[]{email});
        Content subject = new Content().withData(SUBJECT);
        Content htmlBody = new Content().withData(createBody());
        Body body = new Body().withHtml(htmlBody);
        Message message = new Message().withSubject(subject).withBody(body);
        SendEmailRequest request = new SendEmailRequest()
                .withSource(FROM)
                .withDestination(destination)
                .withMessage(message);

        client.sendEmail(request);
    }

    public AmazonSimpleEmailService createClient() {
        return AmazonSimpleEmailServiceClient
                .builder()
                .withCredentials(new ProfileCredentialsProvider())
                .withRegion(Regions.US_EAST_1)
                .build();
    }

}

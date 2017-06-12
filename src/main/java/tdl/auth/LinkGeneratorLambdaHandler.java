package tdl.auth;

import com.amazonaws.services.kms.AWSKMS;
import com.amazonaws.services.kms.AWSKMSClientBuilder;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import ro.ghionoiu.kmsjwt.key.KMSEncrypt;
import ro.ghionoiu.kmsjwt.key.KeyOperationException;
import ro.ghionoiu.kmsjwt.token.JWTEncoder;
import tdl.auth.linkgenerator.Mailer;
import tdl.auth.linkgenerator.Page;

public class LinkGeneratorLambdaHandler implements RequestHandler<Map<String, Object>, String> {

    public static Configuration templateConfiguration;

    private KMSEncrypt kmsEncrypt;

    static {
        templateConfiguration = createDefaultTemplateConfiguration();
    }

    private static Configuration createDefaultTemplateConfiguration() {
        Configuration configuration = new Configuration();
        configuration.setClassForTemplateLoading(LinkGeneratorLambdaHandler.class, "/templates/");
        return configuration;
    }

    private static String getEnv(String key) {
        return Optional.ofNullable(System.getenv(key))
                .orElseThrow(()
                        -> new RuntimeException("[Startup] Environment variable " + key + " not set"));
    }

    @SuppressWarnings("unused")
    public LinkGeneratorLambdaHandler() {
        this(
                getEnv("REGION"),
                getEnv("JWT_ENCRYPT_KEY_ARN"),
                getEnv("BUCKET"),
                getEnv("ACCESS_KEY"),
                getEnv("SECRET_KEY") //TODO: Encrypt this using KMS.
        );
    }

    public LinkGeneratorLambdaHandler(String region, String jwtEncryptKeyArn, String bucket, String accessKey, String secretKey) {
        AWSKMS kmsClient = AWSKMSClientBuilder.standard()
                .withRegion(region)
                .build();
        kmsEncrypt = new KMSEncrypt(kmsClient, jwtEncryptKeyArn);
    }

    @Override
    public String handleRequest(Map<String, Object> request, Context cntxt) {
        try {
            String email = request.get("email").toString();
            String username = request.get("username").toString();
            String challengeId = request.get("challenge").toString();
            int validity = Integer.parseInt(request.get("validity").toString());
            String token = getToken(username, validity);
            Page page = new Page(username, token, getApiEndpointUrl());
            page.generateAndUpload();
            Mailer mailer = new Mailer(email, page.getPublicUrl());
            mailer.send();
            return "OK";
        } catch (IOException | TemplateException | KeyOperationException ex) {
            Logger.getLogger(LinkGeneratorLambdaHandler.class.getName()).log(Level.SEVERE, null, ex);
            return "NOT OK"; //TODO: Fix this
        }
    }

    private String getApiEndpointUrl() {
        return System.getenv("API_ENDPOINT_URL");
    }

    private String getToken(String username, int days) throws KeyOperationException {
        return JWTEncoder.builder(kmsEncrypt)
                .setExpiration(getExpirationDate(days))
                .claim("usr", username)
                .compact();
    }

    private Date getExpirationDate(int days) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DATE, days);
        return calendar.getTime();
    }
}

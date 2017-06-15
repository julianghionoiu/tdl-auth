package tdl.auth;

import com.amazonaws.services.kms.AWSKMS;
import com.amazonaws.services.kms.AWSKMSClientBuilder;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import ro.ghionoiu.kmsjwt.key.KMSEncrypt;
import ro.ghionoiu.kmsjwt.key.KeyOperationException;
import ro.ghionoiu.kmsjwt.token.JWTEncoder;
import tdl.auth.helpers.LambdaExceptionLogger;
import tdl.auth.linkgenerator.Page;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.Optional;

/**
 * This handler receives JSON containing email, username, challengeId, validity.
 */
public class LinkGeneratorLambdaHandler implements RequestHandler<Map<String, Object>, String> {

    public static Configuration templateConfiguration;

    private KMSEncrypt kmsEncrypt;

    static {
        templateConfiguration = createDefaultTemplateConfiguration();
    }

    private final String authEndpointURL;
    private String pageStorageBucket;

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
                getEnv("AUTH_REGION"),
                getEnv("JWT_ENCRYPT_KEY_ARN"),
                getEnv("PAGE_STORAGE_BUCKET"),
                getEnv("AUTH_ENDPOINT_URL")
        );
    }

    private LinkGeneratorLambdaHandler(String region, String jwtEncryptKeyArn, String pageStorageBucket, String authEndpointURL) {
        AWSKMS kmsClient = AWSKMSClientBuilder.standard()
                .withRegion(region)
                .build();
        kmsEncrypt = new KMSEncrypt(kmsClient, jwtEncryptKeyArn);
        this.pageStorageBucket = pageStorageBucket;
        this.authEndpointURL = authEndpointURL;
    }

    @Override
    public String handleRequest(Map<String, Object> request, Context context) {
        try {
            String username = request.get("username").toString();
            String challengeId = request.get("challenge").toString();
            int validity = Integer.parseInt(request.get("validity").toString());
            String token = getToken(username, validity);
            Page page = new Page(username, token, pageStorageBucket, authEndpointURL);
            page.setTemplateConfiguration(templateConfiguration);
            page.generateAndUpload();
            return page.getPublicUrl();
        } catch (IOException | TemplateException | KeyOperationException ex) {
            LambdaExceptionLogger.logException(context, ex);
            return "NOT OK"; //TODO: Fix this
        }
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

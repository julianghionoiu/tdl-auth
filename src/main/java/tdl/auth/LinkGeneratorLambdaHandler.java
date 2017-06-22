package tdl.auth;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.kms.AWSKMS;
import com.amazonaws.services.kms.AWSKMSClientBuilder;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
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
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import tdl.auth.linkgenerator.PageUploader;

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
    private final String pageStorageBucket;
    private PageUploader pageUploader;

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

    @SuppressWarnings({"unused", "WeakerAccess"})
    public LinkGeneratorLambdaHandler() {
        this(
                getEnv("AUTH_REGION"),
                getEnv("JWT_ENCRYPT_KEY_ARN"),
                getEnv("PAGE_STORAGE_BUCKET"),
                getEnv("AUTH_ENDPOINT_URL"),
                getEnv("ACCESS_KEY"),
                getEnv("SECRET_KEY"));
    }

    LinkGeneratorLambdaHandler(String region, String jwtEncryptKeyArn, String pageStorageBucket, String authEndpointURL,
            String accessKey, String secretKey) {
        BasicAWSCredentials awsCreds = new BasicAWSCredentials(accessKey, secretKey);
        AWSCredentialsProvider awsCredential = new AWSStaticCredentialsProvider(awsCreds);
        AWSKMS kmsClient = AWSKMSClientBuilder.standard()
                .withCredentials(awsCredential)
                .withRegion(region)
                .build();
        AmazonS3 s3client = AmazonS3ClientBuilder
                .standard()
                .withCredentials(awsCredential)
                .withRegion(region)
                .build();
        kmsEncrypt = new KMSEncrypt(kmsClient, jwtEncryptKeyArn);
        this.pageStorageBucket = pageStorageBucket;
        this.authEndpointURL = authEndpointURL;
        this.pageUploader = new PageUploader(s3client, pageStorageBucket);
    }

    @Override
    public String handleRequest(Map<String, Object> request, Context context) {
        try {
            return getUploadPageUrlFromRequest(request, context);
        } catch (Exception ex) {
            String type = getLambdaExceptionTypeLabel(ex);
            LambdaExceptionLogger.logException(context, ex);
            throw new RuntimeException("[" + type + "] " + ex.getMessage(), ex);
        }
    }

    public static String getLambdaExceptionTypeLabel(Exception ex) {
        Map<Class, String> map = new HashMap<Class, String>() {{
                put(KeyOperationException.class, "Token");
                put(TemplateException.class, "Email");
                put(IOException.class, "UnknownException");
        }};
        return map.getOrDefault(ex.getClass(), "UnknownException");
    }

    public String getUploadPageUrlFromRequest(Map<String, Object> request, Context context) throws KeyOperationException, IOException, TemplateException {
        String username = request.get("username").toString();
        String challengeId = request.get("challenge").toString();
        int validity = Integer.parseInt(request.get("validity").toString());
        String token = getToken(username, validity);
        context.getLogger().log("username: " + username + ", token: " + token + ", pageStorageBucket: " + pageStorageBucket + ", authEndpointURL: " + authEndpointURL);
        Page page = new Page(username, token, authEndpointURL, templateConfiguration);
        return pageUploader.uploadPage(page);
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

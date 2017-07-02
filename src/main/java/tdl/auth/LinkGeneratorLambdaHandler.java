package tdl.auth;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
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
import tdl.auth.linkgenerator.LinkGeneratorRequest;
import tdl.auth.linkgenerator.Page;

import java.io.IOException;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import tdl.auth.linkgenerator.PageUploader;

/**
 * This handler receives JSON containing email, username, challengeId, validity.
 */
public class LinkGeneratorLambdaHandler implements RequestHandler<LinkGeneratorRequest, String> {

    public static Configuration templateConfiguration;

    private KMSEncrypt kmsEncrypt;

    static {
        templateConfiguration = createDefaultTemplateConfiguration();
    }

    private final String authVerifyEndpointURL;
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
                DefaultAWSCredentialsProviderChain.getInstance());
    }

    LinkGeneratorLambdaHandler(String region, String jwtEncryptKeyArn, String pageStorageBucket, String authVerifyEndpointURL,
                               AWSCredentialsProvider awsCredential) {
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
        this.authVerifyEndpointURL = authVerifyEndpointURL;
        this.pageUploader = new PageUploader(s3client, pageStorageBucket);
    }

    @Override
    public String handleRequest(LinkGeneratorRequest request, Context context) {
        try {
            return getUploadPageUrlFromRequest(request, context);
        } catch (Exception ex) {
            String type = getLambdaExceptionTypeLabel(ex);
            LambdaExceptionLogger.logException(context, ex);
            throw new RuntimeException("[" + type + "] " + ex.getMessage(), ex);
        }
    }

    static String getLambdaExceptionTypeLabel(Exception ex) {
        Map<Class, String> map = new HashMap<Class, String>() {{
                put(IllegalArgumentException.class, "Input");
                put(KeyOperationException.class, "Token");
                put(TemplateException.class, "Email");
                put(IOException.class, "UnknownException");
        }};
        return map.getOrDefault(ex.getClass(), "UnknownException");
    }

    String getUploadPageUrlFromRequest(LinkGeneratorRequest request, Context context) throws KeyOperationException, IOException, TemplateException {
        Predicate<LinkGeneratorRequest> validUsername = req -> req.getUsername() != null;
        if (!validUsername.test(request)) throw new IllegalArgumentException("Not a valid username");

        Predicate<LinkGeneratorRequest> positiveValidity = req -> req.getValidityDays() > 0;
        if (!positiveValidity.test(request)) throw new IllegalArgumentException("Incorrect validity");

        Predicate<LinkGeneratorRequest> nonNullChallenges = req -> req.getChallengeIds() != null && req.getChallengeIds().size() > 0;
        if (!nonNullChallenges.test(request)) throw new IllegalArgumentException("Challenge IDs null or empty");

        String token = getToken(request.getUsername(), request.getValidityDays());
        String sessionId = encodeSessionId(request.getUsername(), request.getChallengeIds());
        context.getLogger().log("username: " + request.getUsername() + ", token: " + token + ", pageStorageBucket: " + pageStorageBucket + ", authVerifyEndpointURL: " + authVerifyEndpointURL);
        Page page = new Page(request.getUsername(), token, sessionId, authVerifyEndpointURL, templateConfiguration);
        return pageUploader.uploadPage(page);
    }

    private String getToken(String username, int days) throws KeyOperationException {
        return JWTEncoder.builder(kmsEncrypt)
                .setExpiration(getExpirationDate(days))
                .claim("usr", username)
                .compact();
    }

    //Debt: This logic needs to be shared between tdl-auth and tdl-server. At the moment it is duplicated
    private static String encodeSessionId(String username, List<String> challenges) {
        String challengeCSV = challenges.stream().collect(Collectors.joining(","));
        String unobfuscatedId = username + "|" + challengeCSV;
        return Base64.getEncoder().encodeToString(unobfuscatedId.getBytes());
    }

    private Date getExpirationDate(int days) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DATE, days);
        return calendar.getTime();
    }
}

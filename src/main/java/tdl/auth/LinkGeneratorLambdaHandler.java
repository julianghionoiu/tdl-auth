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
import tdl.auth.helpers.JWTTdlTokenUtils;
import tdl.auth.helpers.JourneyIdUtils;
import tdl.auth.helpers.LambdaExceptionLogger;
import tdl.auth.linkgenerator.IntroPageParameters;
import tdl.auth.linkgenerator.IntroPageTemplate;
import tdl.auth.linkgenerator.LinkGeneratorRequest;
import tdl.auth.linkgenerator.PageUploader;

import java.io.IOException;
import java.util.*;
import java.util.function.Predicate;

/**
 * This handler receives JSON containing email, username, challengeId, validity.
 */
public class LinkGeneratorLambdaHandler implements RequestHandler<LinkGeneratorRequest, String> {

    private static Configuration templateConfiguration;
    private final IntroPageTemplate introPageTemplate;

    private KMSEncrypt kmsEncrypt;

    static {
        Configuration configuration = new Configuration();
        configuration.setClassForTemplateLoading(LinkGeneratorLambdaHandler.class, "/templates/");
        templateConfiguration = configuration;
    }

    private final String authVerifyEndpointURL;
    private final String pageStorageBucket;
    private PageUploader pageUploader;

    private static String getEnv(String key) {
        return Optional.ofNullable(System.getenv(key))
                .orElseThrow(()
                        -> new RuntimeException("[Startup] Environment variable " + key + " not set"));
    }

    @SuppressWarnings({"unused", "WeakerAccess"})
    public LinkGeneratorLambdaHandler() throws IOException {
        this(
                getEnv("AUTH_REGION"),
                getEnv("JWT_ENCRYPT_KEY_ARN"),
                getEnv("PAGE_STORAGE_BUCKET"),
                getEnv("AUTH_ENDPOINT_URL"),
                DefaultAWSCredentialsProviderChain.getInstance(),
                "intro.html.ftl",
                "../000common");
    }

    LinkGeneratorLambdaHandler(String region, String jwtEncryptKeyArn, String pageStorageBucket, String authVerifyEndpointURL,
                               AWSCredentialsProvider awsCredential, String introPageTemplateName, String resourcesWebPath) throws IOException {
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

        this.introPageTemplate = new IntroPageTemplate(
                introPageTemplateName,
                resourcesWebPath,
                authVerifyEndpointURL);
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
        Predicate<LinkGeneratorRequest> mainChallengeTitle = req -> req.getMainChallengeTitle() != null;
        if (!mainChallengeTitle.test(request)) throw new IllegalArgumentException("Not a valid main challenge title");

        Predicate<LinkGeneratorRequest> sponsorName = req -> req.getSponsorName() != null;
        if (!sponsorName.test(request)) throw new IllegalArgumentException("Not a valid sponsor name");

        Predicate<LinkGeneratorRequest> validUsername = req -> req.getUsername() != null;
        if (!validUsername.test(request)) throw new IllegalArgumentException("Not a valid username");

        Predicate<LinkGeneratorRequest> positiveValidity = req -> req.getValidityDays() > 0;
        if (!positiveValidity.test(request)) throw new IllegalArgumentException("Incorrect validity");

        Predicate<LinkGeneratorRequest> nonNullChallenges = req -> req.getWarmupChallenges() != null;
        if (!nonNullChallenges.test(request)) throw new IllegalArgumentException("Warmup challenges is null");

        Predicate<LinkGeneratorRequest> validChallenge = req -> req.getOfficialChallenge() != null;
        if (!validChallenge.test(request)) throw new IllegalArgumentException("Not a valid official challenge");

        Predicate<LinkGeneratorRequest> codingDurationLabel = req -> req.getCodingDurationLabel() != null;
        if (!codingDurationLabel.test(request)) throw new IllegalArgumentException("Not a valid coding duration label");


        Date expirationDate = getExpirationDate(request.getValidityDays());
        String token = JWTTdlTokenUtils.generate(kmsEncrypt, request.getUsername(), request.getWarmupChallenges(),
                request.getOfficialChallenge(), expirationDate);
        String journeyId = JourneyIdUtils.encode(request.getUsername(), request.getWarmupChallenges(), request.getOfficialChallenge());
        context.getLogger().log(String.format("username: %s, challenge: %s, token: %s, pageStorageBucket: %s, authVerifyEndpointURL: %s",
                request.getUsername(), request.getOfficialChallenge(), token, pageStorageBucket, authVerifyEndpointURL));
        IntroPageParameters introPageParameters = IntroPageParameters.builder()
                .headerImageName(request.getHeaderImageName())
                .mainChallengeTitle(request.getMainChallengeTitle())
                .sponsorName(request.getSponsorName())
                .codingSessionDurationLabel(request.getCodingDurationLabel())
                .enableNoVideoOption(request.getAllowNoVideoOption())
                .username(request.getUsername())
                .challenge(request.getOfficialChallenge())
                .token(token)
                .expirationDate(expirationDate)
                .journeyId(journeyId)
                .build();
        String pageContents = introPageTemplate.generateContent(introPageParameters);
        return pageUploader.uploadPage(pageContents);
    }

    private Date getExpirationDate(int days) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DATE, days);
        return calendar.getTime();
    }
}

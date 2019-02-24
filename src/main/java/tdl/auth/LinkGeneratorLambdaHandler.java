package tdl.auth;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.services.kms.AWSKMS;
import com.amazonaws.services.kms.AWSKMSClientBuilder;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
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

/**
 * This handler receives JSON containing email, username, challengeId, validity.
 */
public class LinkGeneratorLambdaHandler implements RequestHandler<LinkGeneratorRequest, String> {

    private final IntroPageTemplate introPageTemplate;

    private KMSEncrypt kmsEncrypt;

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
        context.getLogger().log("Received request with parameter: " + request);

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
                .inspiredByLabel(request.getInspiredByLabel())
                .codingSessionDurationLabel(request.getCodingDurationLabel())
                .defaultLanguage(request.getDefaultLanguage())
                .enableNoVideoOption(request.getEnableNoVideoOption())
                .enableApplyPressure(request.getEnableApplyPressure())
                .enableReportSharing(request.getEnableReportSharing())
                .username(request.getUsername())
                .challenge(request.getOfficialChallenge())
                .token(token)
                .expirationDate(expirationDate)
                .journeyId(journeyId)
                .build();
        String pageContents = introPageTemplate.generateContent(introPageParameters);
        return pageUploader.uploadPage(request.getUsername(), pageContents);
    }

    private Date getExpirationDate(int days) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DATE, days);
        return calendar.getTime();
    }
}

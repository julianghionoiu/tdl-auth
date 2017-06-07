package tdl.auth;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.EnvironmentVariableCredentialsProvider;
import com.amazonaws.services.kms.AWSKMS;
import com.amazonaws.services.kms.AWSKMSClientBuilder;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import io.jsonwebtoken.Claims;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import org.json.JSONException;
import ro.ghionoiu.kmsjwt.key.KMSDecrypt;
import ro.ghionoiu.kmsjwt.token.JWTDecoder;
import ro.ghionoiu.kmsjwt.token.JWTVerificationException;
import tdl.auth.federated.FederatedUserCredentials;
import tdl.auth.federated.FederatedUserCredentialsProvider;
import tdl.auth.lambda.CredentialInput;

public class LambdaHandler implements RequestStreamHandler {

    private JWTDecoder jwtDecoder;
    private final FederatedUserCredentialsProvider credentialsProvider;

    private static String getEnv(String key) {
        return Optional.ofNullable(System.getenv(key))
                .orElseThrow(() ->
                    new RuntimeException("[Startup] Environment variable " + key + " not set"));
    }

    @SuppressWarnings("unused")
    public LambdaHandler() {
        this(
                getEnv("REGION"),
                getEnv("JWT_DECRYPT_KEY_ARN"),
                getEnv("BUCKET"),
                getEnv("ACCESS_KEY"),
                getEnv("SECRET_KEY") //TODO: Encrypt this using KMS.
        );

    }

    LambdaHandler(String region, String jwtDecryptKeyARN, String bucket, String accessKey, String secretKey) {
        /**
         * A Lambda has temporary credentials obtained by calling AssumeRole on
         * the Execution Role, so we need to use IAM user.
         */
        BasicAWSCredentials awsCreds = new BasicAWSCredentials(accessKey, secretKey);
        AWSCredentialsProvider awsCredential = new AWSStaticCredentialsProvider(awsCreds);
        credentialsProvider = new FederatedUserCredentialsProvider(region, bucket, awsCredential);
        AWSKMS kmsClient = AWSKMSClientBuilder.standard()
                .withRegion(region)
                .build();
        KMSDecrypt kmsDecrypt = new KMSDecrypt(kmsClient, Collections.singleton(jwtDecryptKeyARN));
        jwtDecoder = new JWTDecoder(kmsDecrypt);

    }

    @Override
    public void handleRequest(InputStream inputStream, OutputStream outputStream, Context context) throws IOException {
        try {
            String inputJson = getStringInput(inputStream);
            context.getLogger().log("inputJson:" + inputJson);
            context.getLogger().log("providers:" + credentialsProvider.getBucket() + " " + credentialsProvider.getRegion());
            
            CredentialInput credentialInput = CredentialInput.createFromJsonString(inputJson);
            context.getLogger().log("input:" + credentialInput);
            
            FederatedUserCredentials credentials = createCredentials(credentialInput);
            context.getLogger().log("credentials:" + credentials);
            
            authorize(credentialInput, context.getLogger());
            credentials.save(outputStream);

        } catch (JWTVerificationException e) {
            throw new RuntimeException("[Verification] " + e.getMessage(), e);
        } catch (JSONException e) {
            throw new RuntimeException("[Input] " + e.getMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    private void authorize(CredentialInput credentialInput, LambdaLogger logger) throws JWTVerificationException {
        Claims claims = jwtDecoder.decodeAndVerify(credentialInput.token);
        String principalId = claims.get("usr", String.class);
        logger.log("username:" + credentialInput.username + ",principal: " + principalId);
        if (!Objects.equals(credentialInput.username, principalId)) {
            throw new RuntimeException("[Authorization] User not accepted");
        }
    }

    private static String getStringInput(InputStream inputStream) {
        return new BufferedReader(new InputStreamReader(inputStream))
                .lines()
                .collect(Collectors.joining(""));
    }

    private FederatedUserCredentials createCredentials(CredentialInput input) {
        return new FederatedUserCredentials(credentialsProvider, input.username);
    }

}

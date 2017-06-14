package tdl.auth.linkgenerator;

import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Page {

    public static final long KEY_LENGTH = 32;

    private final String username;

    private final String token;

    private final String apiEndpointUrl;

    private AmazonS3 client;

    private String directory;

    private String content;

    private Configuration templateConfiguration;

    public Page(String username, String token, String apiEndpointUrl) {
        this.username = username;
        this.token = token;
        this.apiEndpointUrl = apiEndpointUrl;
    }

    public void generateAndUpload() throws IOException, TemplateException {
        directory = generateDirectory();
        content = generateContent();
        uploadPage();
    }

    public void setTemplateConfiguration(Configuration templateConfiguration) {
        this.templateConfiguration = templateConfiguration;
    }

    public String generateContent() throws IOException, TemplateException {
        Template template = templateConfiguration.getTemplate("page.html");
        StringWriter stringWriter = new StringWriter();
        Map<String, String> contentParams = new HashMap<>();
        contentParams.put("API_ENDPOINT", apiEndpointUrl);
        contentParams.put("USERNAME", username);
        contentParams.put("TOKEN", token);
        template.process(contentParams, stringWriter);
        return stringWriter.toString();
    }

    public String generateDirectory() {
        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmn1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < KEY_LENGTH) { // length of the random string.
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        String saltStr = salt.toString();
        return saltStr;
    }

    private void uploadPage() {
        client = createClient();
        InputStream stream = new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8));
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType("text/html");
        metadata.setContentDisposition("inline; filename=\"index.html\"");
        PutObjectRequest request = new PutObjectRequest(getBucket(), getFilePath(), stream, metadata)
                .withCannedAcl(CannedAccessControlList.PublicRead)
                .withMetadata(metadata);
        client.putObject(request);
    }

    public String getBucket() {
        return System.getenv("PAGE_STORAGE_BUCKET");
    }

    public String getFilePath() {
        return getDirectory() + "/index.html";
    }

    public String getDirectory() {
        return directory;
    }

    public String getPublicUrl() {
        return client.getUrl(getBucket(), getFilePath()).toString();
    }

    public AmazonS3 createClient() {
        return AmazonS3Client
                .builder()
                .build();
    }
}
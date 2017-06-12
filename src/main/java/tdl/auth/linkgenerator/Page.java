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
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public final class Page {

    private final String username;

    private final String token;

    private final String apiEndpointUrl;

    private final AmazonS3 client;

    private String key;

    private String content;

    private Configuration templateConfiguration;

    public Page(String username, String token, String apiEndpointUrl) {
        this.username = username;
        this.token = token;
        this.apiEndpointUrl = apiEndpointUrl;
        this.client = createClient();
    }

    public void generateAndUpload() throws IOException, TemplateException {
        key = generateKey();
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

    public String generateKey() {
        return "";
    }

    private void uploadPage() {
        InputStream stream = new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8));
        ObjectMetadata metadata = new ObjectMetadata();
        PutObjectRequest request = new PutObjectRequest(getBucket(), getKey(), stream, metadata)
                .withCannedAcl(CannedAccessControlList.PublicRead);
        client.putObject(request);
    }

    public String getBucket() {
        return System.getenv("PAGE_STORAGE_BUCKET");
    }

    public String getKey() {
        return key;
    }

    public String getPublicUrl() {
        return client.getUrl(getBucket(), getKey()).toString();
    }

    public AmazonS3 createClient() {
        return AmazonS3Client
                .builder()
                .withCredentials(new ProfileCredentialsProvider())
                .withRegion(Regions.US_EAST_1)
                .build();
    }
}

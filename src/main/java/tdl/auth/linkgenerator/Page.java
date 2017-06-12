package tdl.auth.linkgenerator;

import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public final class Page {

    private final String email;

    private final AmazonS3 client;

    private String key;

    private String content;

    public Page(String email) {
        this.email = email;
        this.client = createClient();
    }

    public void generateAndUpload() {
        key = generateKey();
        content = generateContent();
        uploadPage();
    }

    private String generateContent() {
        return ""; //TODO: Create HTML in resources
    }

    private String generateKey() {
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

    public URL getPublicUrl() {
        return client.getUrl(getBucket(), getKey());
    }

    public AmazonS3 createClient() {
        return AmazonS3Client
                .builder()
                .withCredentials(new ProfileCredentialsProvider())
                .withRegion(Regions.US_EAST_1)
                .build();
    }
}

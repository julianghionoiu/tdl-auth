package tdl.auth.linkgenerator;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Random;

public class PageUploader {
    private static final long KEY_LENGTH = 6;

    private final AmazonS3 s3client;
    private final String bucket;

    public PageUploader(AmazonS3 s3client, String bucket) {
        this.bucket = bucket;
        this.s3client = s3client;
    }

    public String uploadPage(String username, String pageContents) {
        String salt = generateSalt();
        String path = username +"/"+ salt + "/index.html";

        InputStream stream = new ByteArrayInputStream(pageContents.getBytes(StandardCharsets.UTF_8));
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType("text/html");
        metadata.setContentDisposition("inline; filename=\"index.html\"");
        PutObjectRequest request = new PutObjectRequest(bucket, path, stream, metadata)
                .withMetadata(metadata);
        s3client.putObject(request);
        return s3client.getUrl(bucket, path).toString();
    }

    @SuppressWarnings("FieldCanBeLocal")
    private static String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmn1234567890";
    private static String generateSalt() {
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < KEY_LENGTH) { // length of the random string.
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        return salt.toString();
    }
}

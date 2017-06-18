package tdl.auth.linkgenerator;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Random;
import static tdl.auth.linkgenerator.Page.KEY_LENGTH;

public class PageUploader {

    private final AmazonS3 s3client;
    private final String bucket;

    public PageUploader(AmazonS3 s3client, String bucket) {
        this.bucket = bucket;
        this.s3client = s3client;
    }

    public String uploadPage(Page page) {
        String directory = generateDirectory();
        String path = directory + "/index.html";

        InputStream stream = new ByteArrayInputStream(page.getContent().getBytes(StandardCharsets.UTF_8));
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType("text/html");
        metadata.setContentDisposition("inline; filename=\"index.html\"");
        PutObjectRequest request = new PutObjectRequest(bucket, path, stream, metadata)
                .withCannedAcl(CannedAccessControlList.PublicRead)
                .withMetadata(metadata);
        s3client.putObject(request);
        return s3client.getUrl(bucket, path).toString();
    }

    private String generateDirectory() {
        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmn1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < KEY_LENGTH) { // length of the random string.
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        return salt.toString();
    }
}

package tdl.auth.rules;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.AbortMultipartUploadRequest;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ListMultipartUploadsRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import org.junit.rules.ExternalResource;
import tdl.auth.credentials.AWSSecretPropertiesCredentialsProvider;
import tdl.s3.sync.destination.Destination;
import tdl.s3.sync.destination.S3BucketDestination;

import java.nio.file.Path;
import java.nio.file.Paths;


public class RemoteTestBucket extends ExternalResource {

    private final AmazonS3 amazonS3;
    private final String bucketName;

    private final AWSSecretPropertiesCredentialsProvider secretsProvider;

    //~~~~ Construct
    public RemoteTestBucket() {
        Path privatePropertiesFile = Paths.get(".private", "aws-test-secrets");
        secretsProvider = AWSSecretPropertiesCredentialsProvider.fromPlainTextFile(privatePropertiesFile);

        amazonS3 = AmazonS3ClientBuilder.standard()
                .withCredentials(secretsProvider)
                .withRegion(secretsProvider.getS3Region())
                .build();
        bucketName = secretsProvider.getS3Bucket();

    }

    public AmazonS3 getClient() {
        return amazonS3;
    }

    public AmazonS3 getClient(AWSCredentialsProvider credentialsProvider) {
        return AmazonS3ClientBuilder.standard()
                .withCredentials(credentialsProvider)
                .withRegion(secretsProvider.getS3Region())
                .build();
    }

    public AWSSecretPropertiesCredentialsProvider getSecretsProvider() {
        return secretsProvider;
    }

    //~~~~ Getters

    public Destination asDestination(String folder) {
        return S3BucketDestination.builder()
                .awsClient(amazonS3)
                .bucket(bucketName)
                .prefix(folder)
                .build();
    }

    public Destination asDestination(AWSCredentialsProvider credentialsProvider, String folder) {
        return S3BucketDestination.builder()
                .awsClient(getClient(credentialsProvider))
                .bucket(bucketName)
                .prefix(folder)
                .build();
    }

    //~~~~ Lifecycle management
    @Override
    protected void before() {
        abortAllMultipartUploads();
        removeAllObjects();
    }

    private void removeAllObjects() {
        amazonS3.listObjects(bucketName)
                .getObjectSummaries()
                .forEach(s3ObjectSummary -> {
                    DeleteObjectRequest request = new DeleteObjectRequest(bucketName, s3ObjectSummary.getKey());
                    amazonS3.deleteObject(request);
                });
    }

    private void abortAllMultipartUploads() {
        ListMultipartUploadsRequest multipartUploadsRequest = new ListMultipartUploadsRequest(bucketName);
        amazonS3.listMultipartUploads(multipartUploadsRequest)
                .getMultipartUploads()
                .forEach(upload -> {
                    AbortMultipartUploadRequest request = new AbortMultipartUploadRequest(bucketName, upload.getKey(), upload.getUploadId());
                    amazonS3.abortMultipartUpload(request);
                });
    }

    //~~~~ Bucket actions
    public boolean doesObjectExists(String key) {
        return amazonS3.doesObjectExist(bucketName, key);
    }

    public ObjectMetadata getObjectMetadata(String key) {
        return amazonS3.getObjectMetadata(bucketName, key);
    }

}

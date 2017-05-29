package tdl.auth.rules;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.AbortMultipartUploadRequest;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.InitiateMultipartUploadRequest;
import com.amazonaws.services.s3.model.ListMultipartUploadsRequest;
import org.junit.rules.ExternalResource;


public class RemoteTestBucket extends ExternalResource {
    private final AmazonS3 amazonS3;
    private String bucketName;

    //~~~~ Construct
    public RemoteTestBucket(String region, String bucketName) {
        this.bucketName = bucketName;
        amazonS3 = AmazonS3ClientBuilder.standard()
                .withRegion(region)
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

    public void givenMultipartUploadExists(String testBucketName, String prefix) {
        InitiateMultipartUploadRequest uploadRequest = new InitiateMultipartUploadRequest(testBucketName, prefix + "/test_multipart.dat");
        amazonS3.initiateMultipartUpload(uploadRequest);
    }

    public void givenObjectExists(String testBucketName, String key) {
        amazonS3.putObject(testBucketName, key, "test");
    }
}

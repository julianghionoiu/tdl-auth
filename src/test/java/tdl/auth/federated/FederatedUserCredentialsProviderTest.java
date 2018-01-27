package tdl.auth.federated;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.BasicSessionCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ListMultipartUploadsRequest;
import com.amazonaws.services.s3.model.MultipartUploadListing;
import com.amazonaws.services.securitytoken.model.Credentials;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import tdl.auth.rules.RemoteTestBucket;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static tdl.auth.test.TestConfiguration.*;

public class FederatedUserCredentialsProviderTest {

    private static final String OTHER_USERNAME = "otherusername";

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private AWSCredentialsProvider rootAwsCredential = new AWSStaticCredentialsProvider(new BasicAWSCredentials(
            TEST_ROOT_USER_ACCESS_KEY_ID,
            TEST_ROOT_USER_SECRET_ACCESS_KEY
    ));

    @Rule
    public RemoteTestBucket remoteTestBucket = new RemoteTestBucket(
            TEST_AWS_REGION,
            TEST_VIDEO_STORAGE_BUCKET,
            rootAwsCredential
    );

    // Software under test
    private FederatedUserCredentialsProvider federatedUserCredentialsProvider
            = new FederatedUserCredentialsProvider(TEST_AWS_REGION, TEST_VIDEO_STORAGE_BUCKET,
            TEST_TDL_SCOPE, rootAwsCredential);
    private AmazonS3 federatedS3Client;

    @Before
    public void setUp() {
        Credentials federatedUserCredentials = federatedUserCredentialsProvider
                .getFederatedTokenFor(TEST_CHALLENGE, TEST_USERNAME)
                .getCredentials();
        federatedS3Client = AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(new BasicSessionCredentials(
                        federatedUserCredentials.getAccessKeyId(),
                        federatedUserCredentials.getSecretAccessKey(),
                        federatedUserCredentials.getSessionToken())))
                .withRegion(TEST_AWS_REGION)
                .build();
    }

    //~~~~ Unit test
    @Test
    public void basicConstructor() {
        expectNoException();
        new FederatedUserCredentialsProvider(TEST_AWS_REGION, TEST_VIDEO_STORAGE_BUCKET, TEST_TDL_SCOPE);
    }

    //~~~~ Object listing permissions
    @Test
    public void should_have_permission_to_list_own_challenge_bucket() {
        expectNoException();
        federatedS3Client.listObjects(TEST_VIDEO_STORAGE_BUCKET, TEST_CHALLENGE + "/" + TEST_USERNAME + "/");
    }

    @Test
    public void should_not_have_permission_to_list_whole_bucket() {
        expectForbiddenException();
        federatedS3Client.listObjects(TEST_VIDEO_STORAGE_BUCKET, "");
    }

    @Test
    public void should_not_have_permission_to_list_other_folders() {
        expectForbiddenException();
        federatedS3Client.listObjects(TEST_VIDEO_STORAGE_BUCKET, "other_folder");
    }

    //~~~~ Object retrieving permissions
    @Test
    public void should_have_permission_to_get_object_in_own_challenge_folder() {
        String ownFile = TEST_CHALLENGE + "/" + TEST_USERNAME + "/my_file.txt";
        remoteTestBucket.givenObjectExists(TEST_VIDEO_STORAGE_BUCKET, ownFile);

        expectNoException();
        federatedS3Client.getObject(TEST_VIDEO_STORAGE_BUCKET, ownFile);
    }

    @Test
    public void should_not_have_permission_to_get_objects_in_other_challenge_folders() {
        String otherFile = TEST_CHALLENGE + "/" + OTHER_USERNAME + "/my_file.txt";
        remoteTestBucket.givenObjectExists(TEST_VIDEO_STORAGE_BUCKET, otherFile);

        expectForbiddenException();
        federatedS3Client.getObject(TEST_VIDEO_STORAGE_BUCKET, otherFile);
    }

    //~~~~ Multipart listing permissions
    @Test
    public void should_have_permission_to_list_own_multipart_uploads() {
        remoteTestBucket.givenMultipartUploadExists(TEST_VIDEO_STORAGE_BUCKET, TEST_CHALLENGE + "/" + TEST_USERNAME + "/upload.txt");
        ListMultipartUploadsRequest request = new ListMultipartUploadsRequest(TEST_VIDEO_STORAGE_BUCKET);
        request.setPrefix(TEST_CHALLENGE + "/" + TEST_USERNAME);

        expectNoException();
        MultipartUploadListing multipartUploadListing = federatedS3Client.listMultipartUploads(request);
        assertThat(multipartUploadListing.getMultipartUploads().size(), is(1));
    }

    @Ignore("AWS bug. See: https://forums.aws.amazon.com/thread.jspa?threadID=158131")
    @Test
    public void should_not_have_permission_to_list_other_multipart_uploads() {
        remoteTestBucket.givenMultipartUploadExists(TEST_VIDEO_STORAGE_BUCKET, TEST_CHALLENGE + "/" + OTHER_USERNAME + "/upload.txt");
        ListMultipartUploadsRequest request = new ListMultipartUploadsRequest(TEST_VIDEO_STORAGE_BUCKET);
        request.setPrefix(OTHER_USERNAME);

        expectForbiddenException();
        federatedS3Client.listMultipartUploads(request);
    }

    //~~~~ Part listing permissions
    @Test
    public void should_have_permission_to_upload_parts_to_uploads_for_own_challenge_folder() {
        //TODO
    }

    @Test
    public void should_not_have_permission_to_upload_parts_to_other_uploads() {
        //TODO
    }

    //~~~~ Upload permission
    @Test
    public void should_have_permission_to_upload_to_its_own_challenge_s3_folder() {
        String key = TEST_CHALLENGE + "/" + TEST_USERNAME + "/test_write.txt";

        federatedS3Client.putObject(TEST_VIDEO_STORAGE_BUCKET, key, "test");

        assertThat(remoteTestBucket.doesObjectExists(key), is(true));
    }

    @Test
    public void should_not_have_permission_to_upload_to_other_challenge_folders() {
        expectForbiddenException();
        federatedS3Client.putObject(TEST_VIDEO_STORAGE_BUCKET, TEST_CHALLENGE + "/" + OTHER_USERNAME + "/test_write.txt", "test");
    }

    //~~~~ Helpers
    private void expectNoException() {
        //No exception
    }

    private void expectForbiddenException() {
        thrown.expect(AmazonServiceException.class);
        thrown.expectMessage(containsString("Access Denied"));
    }
}

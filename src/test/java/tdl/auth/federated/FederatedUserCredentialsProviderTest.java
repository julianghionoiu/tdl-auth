package tdl.auth.federated;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
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

import java.util.Optional;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class FederatedUserCredentialsProviderTest {
    private static final String TEST_AWS_REGION = Optional.ofNullable(System.getenv("TEST_AWS_REGION"))
            .orElse("eu-west-2");
    private static final String TEST_BUCKET_NAME = Optional.ofNullable(System.getenv("TEST_BUCKET_NAME"))
            .orElse("tdl-test-auth");
    private static final String TEST_USERNAME = "tdl-test-user";
    private static final String OTHER_USERNAME = "other_user";

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Rule
    public RemoteTestBucket remoteTestBucket = new RemoteTestBucket(TEST_AWS_REGION, TEST_BUCKET_NAME);

    // Software under test
    private FederatedUserCredentialsProvider federatedUserCredentialsProvider =
            new FederatedUserCredentialsProvider(TEST_AWS_REGION, TEST_BUCKET_NAME);
    private AmazonS3 federatedS3Client;


    @Before
    public void setUp() throws Exception {
        Credentials federatedUserCredentials = federatedUserCredentialsProvider
                .getFederatedTokenFor(TEST_USERNAME)
                .getCredentials();
        federatedS3Client = AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(new BasicSessionCredentials(
                        federatedUserCredentials.getAccessKeyId(),
                        federatedUserCredentials.getSecretAccessKey(),
                        federatedUserCredentials.getSessionToken())))
                .withRegion(TEST_AWS_REGION)
                .build();
    }

    //~~~~ Object listing permissions

    @Test
    public void should_have_permission_to_list_own_bucket() throws Exception {
        expectNoException();
        federatedS3Client.listObjects(TEST_BUCKET_NAME, TEST_USERNAME+"/");
    }

    @Test
    public void should_not_have_permission_to_list_whole_bucket() throws Exception {
        expectForbiddenException();
        federatedS3Client.listObjects(TEST_BUCKET_NAME, "");
    }

    @Test
    public void should_not_have_permission_to_list_other_folders() throws Exception {
        expectForbiddenException();
        federatedS3Client.listObjects(TEST_BUCKET_NAME, "other_folder");
    }

    //~~~~ Object retrieving permissions

    @Test
    public void should_have_permission_to_get_object_in_own_folder() throws Exception {
        String ownFile = TEST_USERNAME + "/my_file.txt";
        remoteTestBucket.givenObjectExists(TEST_BUCKET_NAME, ownFile);

        expectNoException();
        federatedS3Client.getObject(TEST_BUCKET_NAME, ownFile);
    }

    @Test
    public void should_not_have_permission_to_get_objects_in_other_folders() throws Exception {
        String otherFile = OTHER_USERNAME + "/my_file.txt";
        remoteTestBucket.givenObjectExists(TEST_BUCKET_NAME, otherFile);

        expectForbiddenException();
        federatedS3Client.getObject(TEST_BUCKET_NAME, otherFile);
    }

    //~~~~ Multipart listing permissions

    @Test
    public void should_have_permission_to_list_own_multipart_uploads() throws Exception {
        remoteTestBucket.givenMultipartUploadExists(TEST_BUCKET_NAME, TEST_USERNAME + "/upload.txt");
        ListMultipartUploadsRequest request = new ListMultipartUploadsRequest(TEST_BUCKET_NAME);
        request.setPrefix(TEST_USERNAME);

        expectNoException();
        MultipartUploadListing multipartUploadListing = federatedS3Client.listMultipartUploads(request);
        assertThat(multipartUploadListing.getMultipartUploads().size(), is(1));
    }

    @Ignore("AWS bug. See: https://forums.aws.amazon.com/thread.jspa?threadID=158131")
    @Test
    public void should_not_have_permission_to_list_other_multipart_uploads() throws Exception {
        remoteTestBucket.givenMultipartUploadExists(TEST_BUCKET_NAME, OTHER_USERNAME + "/upload.txt");
        ListMultipartUploadsRequest request = new ListMultipartUploadsRequest(TEST_BUCKET_NAME);
        request.setPrefix(OTHER_USERNAME);

        expectForbiddenException();
        federatedS3Client.listMultipartUploads(request);
    }

    //~~~~ Part listing permissions

    @Test
    public void should_have_permission_to_upload_parts_to_uploads_for_own_folder() throws Exception {
        //TODO
    }

    @Test
    public void should_not_have_permission_to_upload_parts_to_other_uploads() throws Exception {
        //TODO
    }


    //~~~~ Upload permission

    @Test
    public void should_have_permission_to_upload_to_its_own_s3_folder() throws Exception {
        String key = TEST_USERNAME + "/test_write.txt";

        federatedS3Client.putObject(TEST_BUCKET_NAME, key, "test");

        assertThat(remoteTestBucket.doesObjectExists(key), is(true));
    }

    @Test
    public void should_not_have_permission_to_upload_to_other_folders() throws Exception {
        expectForbiddenException();
        federatedS3Client.putObject(TEST_BUCKET_NAME, OTHER_USERNAME + "/test_write.txt", "test");
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
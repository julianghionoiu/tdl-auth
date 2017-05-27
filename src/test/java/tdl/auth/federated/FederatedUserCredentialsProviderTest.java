package tdl.auth.federated;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import tdl.auth.rules.RemoteTestBucket;
import tdl.auth.s3.DefaultS3FolderPolicy;
import tdl.s3.sync.Filters;
import tdl.s3.sync.RemoteSync;
import tdl.s3.sync.Source;

import java.nio.file.Path;
import java.nio.file.Paths;

import static org.hamcrest.CoreMatchers.anyOf;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class FederatedUserCredentialsProviderTest {

    @Rule
    public RemoteTestBucket remoteTestBucket = new RemoteTestBucket();

    private Source source;
    private String s3Bucket;
    private String test_user;
    private FederatedUserCredentialsProvider federatedUserCredentialsProvider;


    @Before
    public void setUp() throws Exception {
        String s3Region = remoteTestBucket.getSecretsProvider().getS3Region();
        s3Bucket = remoteTestBucket.getSecretsProvider().getS3Bucket();
        test_user = "test_user";

        Path path = Paths.get("src/test/resources/test_1/");
        Filters filters = Filters.getBuilder().include(Filters.endsWith("txt")).create();
        source = Source.getBuilder(path)
                .setFilters(filters)
                .create();

        //Get temporal credentials for user
        federatedUserCredentialsProvider = FederatedUserCredentialsProvider.builder()
                .iamUserCredentialsProvider(remoteTestBucket.getSecretsProvider())
                .region(s3Region)
                .userName(test_user)
                .policy(DefaultS3FolderPolicy.getForUser(s3Bucket, test_user))
                .build();
    }

    @Test
    public void should_get_credentials_with_access_to_write_file_to_s3() throws Exception {
        //Prepare and upload file with temporal credentials to user's folder
        RemoteSync sync = new RemoteSync(source, remoteTestBucket.asDestination(federatedUserCredentialsProvider, test_user + "/"));
        sync.run();

        //Check that file exists in appropriate folder
        assertThat(remoteTestBucket.doesObjectExists(test_user + "/file.txt"), is(true));
    }

    @Test
    public void should_have_possibility_to_list_own_bucket() throws Exception {
        //Upload file to user's folder with federated user
        new RemoteSync(source, remoteTestBucket.asDestination(federatedUserCredentialsProvider, test_user + "/")).run();

        //Check that user can list own folder
        ListObjectsRequest request = new ListObjectsRequest(s3Bucket, test_user, null, null, null);
        ObjectListing objectListing = remoteTestBucket.getClient(federatedUserCredentialsProvider).listObjects(request);
        assertThat(objectListing.getObjectSummaries().size(), is(1));
    }

    @Test
    public void should_not_have_possibility_to_list_whole_bucket() throws Exception {
        //Upload file to root with IAM user
        new RemoteSync(source, remoteTestBucket.asDestination("")).run();
        //Upload file to folder with IAM user
        new RemoteSync(source, remoteTestBucket.asDestination("other_folder/")).run();

        //Check that user can't list whole bucket
        int errorCounter = 0;
        try {
            remoteTestBucket.getClient(federatedUserCredentialsProvider).listObjects(s3Bucket);
        } catch (Exception e) {
            //Check that error was due to insufficient access rights
            assertThat(e.getMessage(), anyOf(containsString("Forbidden"), containsString("AccessDenied")));
            errorCounter++;
        }
        //Check that user can't list other folder
        try {
            ListObjectsRequest request = new ListObjectsRequest(s3Bucket, "other_folder", null, null, null);
            remoteTestBucket.getClient(federatedUserCredentialsProvider).listObjects(request);
        } catch (Exception e) {
            //Check that error was due to insufficient access rights
            assertThat(e.getMessage(), anyOf(containsString("Forbidden"), containsString("AccessDenied")));
            errorCounter++;
        }
        assertThat(errorCounter, is(2));
    }

    @Test
    @Ignore("TODO restrict user to see only own uploads")
    public void should_not_have_possibility_to_list_others_multipart_uploads() throws Exception {
        //Start multipart upload with IAM user
        remoteTestBucket.getClient().initiateMultipartUpload(new InitiateMultipartUploadRequest(s3Bucket, "test_multipart.dat"));
        //ensure IAM user can see it
        ListMultipartUploadsRequest request = new ListMultipartUploadsRequest(s3Bucket);
        MultipartUploadListing multipartUploadListing = remoteTestBucket.getClient().listMultipartUploads(request);
        assertThat(multipartUploadListing.getMultipartUploads().size(), is(1));

        //ensure federated user do not have permissions to see this
        int errorCounter = 0;
        try {
            remoteTestBucket.getClient(federatedUserCredentialsProvider).listMultipartUploads(request);
        } catch (Exception e) {
            //Check that error was due to insufficient access rights
            assertThat(e.getMessage(), anyOf(containsString("Forbidden"), containsString("AccessDenied")));
            errorCounter++;
        }
        assertThat(errorCounter, is(1));
    }

    @Test
    @Ignore("TODO restrict user to see only own uploads")
    public void should_have_possibility_to_list_own_multipart_uploads() throws Exception {
        AmazonS3 client = remoteTestBucket.getClient(federatedUserCredentialsProvider);
        //Start multipart upload with federated user to user's folder
        InitiateMultipartUploadRequest uploadRequest = new InitiateMultipartUploadRequest(s3Bucket, test_user + "/test_multipart.dat");
        client.initiateMultipartUpload(uploadRequest);
        //ensure federated user can see it
        ListMultipartUploadsRequest request = new ListMultipartUploadsRequest(s3Bucket);
        request.setPrefix(test_user);
        MultipartUploadListing multipartUploadListing = client.listMultipartUploads(request);
        assertThat(multipartUploadListing.getMultipartUploads().size(), is(1));
    }

    @Test
    public void should_get_credentials_without_access_to_other_folders() throws Exception {
        int errorCounter = 0;
        //Try to upload file to other than user's folder
        try {
            RemoteSync sync = new RemoteSync(source, remoteTestBucket.asDestination(federatedUserCredentialsProvider, "other_folder/"));
            sync.run();
        } catch (Exception e) {
            //Check that error was due to insufficient access rights
            assertThat(e.getMessage(), anyOf(containsString("Forbidden"), containsString("AccessDenied")));
            errorCounter++;
        }

        //Try to upload file to root folder
        try {
            RemoteSync sync = new RemoteSync(source, remoteTestBucket.asDestination(federatedUserCredentialsProvider, ""));
            sync.run();
        } catch (Exception e) {
            //Check that error was due to insufficient access rights
            assertThat(e.getMessage(), anyOf(containsString("Forbidden"), containsString("AccessDenied")));
            errorCounter++;
        }

        //Check that there was 2 errors while uploading files
        assertThat(errorCounter, is(2));

        //Check that files were not uploaded
        assertThat(remoteTestBucket.doesObjectExists("other_folder/file.txt"), is(false));
        assertThat(remoteTestBucket.doesObjectExists("file.txt"), is(false));
    }


}
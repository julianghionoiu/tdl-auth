package tdl.auth.federated;

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


    @Test
    public void should_get_credentials_with_access_to_write_file_to_s3 () throws Exception {

        String s3Region = remoteTestBucket.getSecretsProvider().getS3Region();
        String s3Bucket = remoteTestBucket.getSecretsProvider().getS3Bucket();
        String test_user = "test_user";

        //Get temporal credentials for user
        FederatedUserCredentialsProvider federatedUserCredentialsProvider = FederatedUserCredentialsProvider.builder()
                .iamUserCredentialsProvider(remoteTestBucket.getSecretsProvider())
                .region(s3Region)
                .userName(test_user)
                .policy(DefaultS3FolderPolicy.getForUser(s3Bucket, test_user))
                .build();

        //Prepare and upload file with temporal credentials to user's folder
        Path path = Paths.get("src/test/resources/test_1/");
        Filters filters = Filters.getBuilder().include(Filters.endsWith("txt")).create();
        Source source = Source.getBuilder(path)
                .setFilters(filters)
                .create();
        RemoteSync sync = new RemoteSync(source, remoteTestBucket.asDestination(federatedUserCredentialsProvider, test_user + "/"));
        sync.run();

        //Check that file exists in appropriate folder
        assertThat(remoteTestBucket.doesObjectExists(test_user + "/file.txt"), is(true));
    }

    @Test
    public void should_get_credentials_without_access_to_other_folders () throws Exception {

        String s3Region = remoteTestBucket.getSecretsProvider().getS3Region();
        String s3Bucket = remoteTestBucket.getSecretsProvider().getS3Bucket();
        String test_user = "test_user_25";

        FederatedUserCredentialsProvider federatedUserCredentialsProvider = FederatedUserCredentialsProvider.builder()
                .iamUserCredentialsProvider(remoteTestBucket.getSecretsProvider())
                .region(s3Region)
                .userName(test_user)
                .policy(DefaultS3FolderPolicy.getForUser(s3Bucket, test_user))
                .build();

        Path path = Paths.get("src/test/resources/test_1/");
        Filters filters = Filters.getBuilder().include(Filters.endsWith("txt")).create();
        Source source = Source.getBuilder(path)
                .setFilters(filters)
                .create();


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
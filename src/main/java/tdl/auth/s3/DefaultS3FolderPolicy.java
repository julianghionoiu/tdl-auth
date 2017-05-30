package tdl.auth.s3;

import com.amazonaws.auth.policy.Condition;
import com.amazonaws.auth.policy.Policy;
import com.amazonaws.auth.policy.Resource;
import com.amazonaws.auth.policy.Statement;

import java.util.Arrays;

public class DefaultS3FolderPolicy {

    public static Policy getForUser(String bucket, String userName) {
        Statement creatingObjectsStatement = getObjectCreatingStatement(bucket, userName);
        Statement multipartUploadStatement = getMultipartUploadStatement(bucket, userName);
        Statement listBucketStatement = getListBucketStatement(bucket, userName);

        return new Policy("PerUserFileUploadingPolicy", Arrays.asList(multipartUploadStatement, creatingObjectsStatement, listBucketStatement));
    }

    private static Statement getObjectCreatingStatement(String bucket, String userName) {
        return new Statement(Statement.Effect.Allow)
                .withActions(
                        () -> "s3:PutObject",
                        () -> "s3:GetObject"
                )
                .withResources(new Resource("arn:aws:s3:::" + bucket + "/" + userName + "/*"));
    }

    private static Statement getMultipartUploadStatement(String bucket, String userName) {
        return new Statement(Statement.Effect.Allow)
                .withActions(() -> "s3:ListBucketMultipartUploads")
                .withResources(new Resource("arn:aws:s3:::" + bucket));
    }

    private static Statement getListBucketStatement(String bucket, String userName) {
        return new Statement(Statement.Effect.Allow)
                .withActions(
                        () -> "s3:ListBucket"
                )
                .withResources(new Resource("arn:aws:s3:::" + bucket))
                .withConditions(
                        new Condition()
                                .withType("StringEquals")
                                .withConditionKey("s3:prefix")
                                .withValues(userName+"/")
                );
    }
}

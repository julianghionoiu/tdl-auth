package tdl.auth.s3;

import com.amazonaws.auth.policy.Condition;
import com.amazonaws.auth.policy.Policy;
import com.amazonaws.auth.policy.Resource;
import com.amazonaws.auth.policy.Statement;

import java.util.Arrays;

public class DefaultS3FolderPolicy {

    public static Policy getForUser(String bucket, String userName) {
        Statement creatingObjectsStatement = getObjectCreatingStatement(bucket);
        Statement multipartUploadStatement = getMultipartUploadStatement(bucket, userName);

        return new Policy("Files uploading policy", Arrays.asList(multipartUploadStatement, creatingObjectsStatement));
    }

    private static Statement getObjectCreatingStatement(String bucket) {
        return new Statement(Statement.Effect.Allow)
                .withActions(
                        () -> "s3:PutObject",
                        () -> "s3:GetObject"
                )
                .withResources(new Resource("arn:aws:s3:::" + bucket + "/${aws:username}/*"));
    }

    private static Statement getMultipartUploadStatement(String bucket, String userName) {
        return new Statement(Statement.Effect.Allow)
                .withActions(
                        () -> "s3:ListBucket",
                        () -> "s3:ListBucketMultipartUploads"
                )
                .withResources(new Resource("arn:aws:s3:::" + bucket + "/${aws:username}/*"))
                .withConditions(
                        new Condition()
                                .withType("StringEquals")
                                .withConditionKey("s3:prefix")
                                .withValues(userName)
                );
    }
}

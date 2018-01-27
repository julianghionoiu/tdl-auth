package tdl.auth.federated;

import com.amazonaws.auth.policy.Condition;
import com.amazonaws.auth.policy.Policy;
import com.amazonaws.auth.policy.Resource;
import com.amazonaws.auth.policy.Statement;

import java.util.Arrays;

class DefaultS3FolderPolicy {

    static Policy allowAccessToPath(String bucket, String s3BucketKey) {
        Statement creatingObjectsStatement = getObjectCreatingStatement(bucket, s3BucketKey);
        Statement multipartUploadStatement = getMultipartUploadStatement(bucket);
        Statement listBucketStatement = getListBucketStatement(bucket, s3BucketKey);

        return new Policy("PerUserFileUploadingPolicy", Arrays.asList(multipartUploadStatement, creatingObjectsStatement, listBucketStatement));
    }

    private static Statement getObjectCreatingStatement(String bucket, String key) {
        return new Statement(Statement.Effect.Allow)
                .withActions(
                        () -> "s3:PutObject",
                        () -> "s3:GetObject"
                )
                .withResources(new Resource("arn:aws:s3:::" + bucket + "/" + key + "/*"));
    }

    private static Statement getMultipartUploadStatement(String bucket) {
        return new Statement(Statement.Effect.Allow)
                .withActions(() -> "s3:ListBucketMultipartUploads")
                .withResources(new Resource("arn:aws:s3:::" + bucket));
    }

    private static Statement getListBucketStatement(String bucket, String key) {
        return new Statement(Statement.Effect.Allow)
                .withActions(
                        () -> "s3:ListBucket"
                )
                .withResources(new Resource("arn:aws:s3:::" + bucket))
                .withConditions(
                        new Condition()
                                .withType("StringEquals")
                                .withConditionKey("s3:prefix")
                                .withValues(key+"/")
                );
    }
}

#!/usr/bin/env bash

source ./configs/deploy/env.sh
STACK_OPT="--stack-name ${STACK_NAME}"
AWS_CF_EXEC="aws ${AWS_S3_CONFIG} cloudformation"

echo "Uploading Zip to S3"
aws $AWS_S3_CONFIG s3 cp \
    ./build/distributions/tld-auth-lambda-0.0.1.zip \
    $ENV_DEPLOY_S3_PATH

#TODO: Handle update if stack already exists.
$AWS_CF_EXEC delete-stack $STACK_OPT
echo "Waiting for Cloud Formation to delete..."
$AWS_CF_EXEC wait stack-delete-complete $STACK_OPT
echo "Deleted!"

#$AWS_CF_EXEC describe-stacks $STACK_OPT >  /dev/null 2>&1

echo "Creating CloudFormation..."
$AWS_CF_EXEC create-stack \
    $STACK_OPT \
    --template-body file://etc/cloudformation.yml \
    --parameters file://configs/deploy/parameters.json

echo "Waiting for Cloud Formation..."
$AWS_CF_EXEC wait stack-create-complete $STACK_OPT
echo "Deployed!"

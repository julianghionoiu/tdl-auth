#!/usr/bin/env bash

#ENV_DEPLOY_S3_PATH=s3://bucket/
#AWS_S3_CONFIG=""
#STACK_NAME=testing-tdl-auth

STACK_OPT="--stack-name ${STACK_NAME}"
AWS_CF_EXEC="aws ${AWS_S3_CONFIG} cloudformation"

echo "Uploading Zip to S3"
aws $AWS_S3_CONFIG s3 cp \
    ./build/distributions/tld-auth-lambda-0.0.1.zip \
    $ENV_DEPLOY_S3_PATH

$AWS_CF_EXEC describe-stacks $STACK_OPT >  /dev/null 2>&1

echo "Creating CloudFormation..."
$AWS_CF_EXEC create-stack \
    $STACK_OPT \
    --template-body file://etc/cloudformation.yml \
    --parameters file://etc/parameters.json
$AWS_CF_EXEC wait stack-create-complete $STACK_OPT
echo "Deployed"
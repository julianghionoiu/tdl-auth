#!/usr/bin/env bash

source ./configs/deploy/env.sh
STACK_OPT="--stack-name ${STACK_NAME}"
AWS_CF_EXEC="aws ${AWS_S3_CONFIG} cloudformation"

echo "Uploading Zip to S3"
aws $AWS_S3_CONFIG s3 sync \
    ./build/distributions/ \
    $ENV_DEPLOY_S3_PATH \
    --include "*.zip" #No deploying if already exists similar copy

$AWS_CF_EXEC describe-stacks $STACK_OPT > /dev/null  2>&1

if [ $? -eq 0 ]; then
    echo "Stack exists. Updating..."
    AWS_CF_COMMAND=update-stack
elif [ $? -eq 255 ]; then
    echo "Stack does not exists. Creating..."
    AWS_CF_COMMAND=create-stack
else
    echo "Unhandled exception!"
    exit 1
fi

$AWS_CF_EXEC $AWS_CF_COMMAND \
    $STACK_OPT \
    --template-body file://etc/cloudformation.yml \
    --parameters file://configs/deploy/parameters.json \
    2>/dev/null

if [ $? -eq 255 ] && [ "$AWS_CF_COMMAND" == "update-stack" ]; then
    echo "No update required"
    exit 0
fi
echo "Waiting for Cloud Formation..."
$AWS_CF_EXEC wait stack-create-complete $STACK_OPT
echo "Deployed!"

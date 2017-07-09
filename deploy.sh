#!/usr/bin/env bash

set -x

if [ $# -eq 0 ]
  then
    echo "Usage: $0 profile"
fi
PROFILE=$1

source ./configs/deploy/env-${PROFILE}.sh
STACK_OPT="--stack-name ${STACK_NAME}"
AWS_CF_EXEC="aws ${AWS_CONFIG} cloudformation"
LAMBDA_CODE=tld-auth-lambda-0.0.1.zip

echo "Uploading Zip to S3..."
SYNC_OUTPUT=$(aws ${AWS_CONFIG} s3 sync \
    ./build/distributions/ \
    s3://${ENV_DEPLOY_S3_BUCKET}/${ENV_DEPLOY_S3_PREFIX} \
    --include "*.zip") #No deploying if already exists similar copy
#SYNC_OUTPUT is empty if no file to sync
if [ -n "${SYNC_OUTPUT}" ]; then
    echo "Zip uploaded"
    ZIP_UPLOADED=1
else
    echo "Zip file already exists"
    ZIP_UPLOADED=0
fi

${AWS_CF_EXEC} describe-stacks ${STACK_OPT} > /dev/null  2>&1
EXISTS=$?
if [ ${EXISTS} -eq 0 ]; then
    echo "Stack exists. Updating..."
    AWS_CF_COMMAND=update-stack
    AWS_WAIT_COMMAND=stack-update-complete
elif [ ${EXISTS} -eq 255 ]; then
    echo "Stack does not exists. Creating..."
    AWS_CF_COMMAND=create-stack
    AWS_WAIT_COMMAND=stack-create-complete
else
    echo "Unhandled exception!"
    exit 1
fi

${AWS_CF_EXEC} ${AWS_CF_COMMAND} \
    ${STACK_OPT} \
    --template-body file://etc/cloudformation.yml \
    --parameters file://configs/deploy/parameters-${PROFILE}.json \
    2>&1

if [ $? -eq 255 ] && [ "$AWS_CF_COMMAND" == "update-stack" ]; then
    echo "No update required"
    if [ $ZIP_UPLOADED -eq 1 ]; then
        echo "Reupload lambda"
        STACK_DESCRIPTION=$(${AWS_CF_EXEC} describe-stacks ${STACK_OPT})
        FUNCTION_NAMES=$(echo ${STACK_DESCRIPTION} | jq ".Stacks[0].Parameters[] | select(.ParameterKey|contains(\"FunctionName\")) | .ParameterValue" -r)

        for FUNCTION_NAME in ${FUNCTION_NAMES}; do
            echo "Updating function... $FUNCTION_NAME"
            aws ${AWS_CONFIG} lambda update-function-code \
                --function-name $FUNCTION_NAME \
                --s3-bucket $ENV_DEPLOY_S3_BUCKET \
                --s3-key $ENV_DEPLOY_S3_PREFIX$LAMBDA_CODE
        done
    fi
else
    echo "Waiting for Cloud Formation..."

    ${AWS_CF_EXEC} wait ${AWS_WAIT_COMMAND} $STACK_OPT
    echo "Deployed!"
fi

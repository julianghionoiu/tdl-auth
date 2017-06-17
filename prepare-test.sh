#!/usr/bin/env bash
################################################################################
# To export, execute `. ./prepare-test.sh`. See the `.` in the front.
################################################################################
TEST_CLOUD_FORMATION_PATH=src/test/resources/cloudformation.yml
AWS_CONFIG=${AWS_CONFIG:-'--profile personal --region ap-southeast-1'}
STACK_NAME=${STACK_NAME:-'test1'}
AWS_CF_STACK_OPT="--stack-name $STACK_NAME"
AWS_CF_EXEC="aws ${AWS_CONFIG} cloudformation"

USER_ARN=$(aws ${AWS_CONFIG} iam get-user | jq '.User.Arn')

${AWS_CF_EXEC} describe-stacks $AWS_CF_STACK_OPT > /dev/null  2>&1

if [ $? -eq 255 ]; then
    echo "Stack does not exist. Creating..."
    ${AWS_CF_EXEC} create-stack $AWS_CF_STACK_OPT \
        --template-body file://src/test/resources/cloudformation.yml \
        --capabilities CAPABILITY_IAM \
        --parameters ParameterKey=CurrentUserArn,ParameterValue=$USER_ARN,UsePreviousValue=false
    echo "Waiting..."
    ${AWS_CF_EXEC} wait stack-create-complete $AWS_CF_STACK_OPT
    echo "Stack created!"
else
    echo "Stack ${STACK_NAME} already exists."
fi

echo "Exporting environment"
STACK_DESCRIPTION=$(${AWS_CF_EXEC} describe-stacks $AWS_CF_STACK_OPT)
STACK_OUTPUTS=$(echo $STACK_DESCRIPTION | jq ".Stacks[0].Outputs")

declare -A KEYS
KEYS=( \
    [TEST_USERNAME]=TestUserName \
    [TEST_JWT_KEY_ARN]=TestEncryptKeyArn \
    [TEST_BUCKET]=TestBucketName \
    [TEST_ROOT_USER_ACCESS_KEY_ID]=TestRootUserAccessKeyId \
    [TEST_ROOT_USER_SECRET_KEY_ID]=TestRootUserSecretKeyId \
    [TEST_USER_ACCESS_KEY_ID]=TestUserAccessKeyId \
    [TEST_USER_SECRET_KEY_ID]=TestUserSecretKeyId \
    [TEST_AWS_REGION]=TestRegion \
)
echo "" > src/test/resources/configuration.properties
for KEY in "${!KEYS[@]}"
do
    VALUE=$(echo $STACK_OUTPUTS | jq ".[] | select(.OutputKey==\"${KEYS[$KEY]}\") | .OutputValue" -r)
    echo "${KEY}=${VALUE}" >> src/test/resources/configuration.properties
done
echo "Test Environment keys exported!"

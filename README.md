[![Java Version](http://img.shields.io/badge/Java-1.8-blue.svg)](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)
[![Codeship Status for julianghionoiu/tdl-auth](https://img.shields.io/codeship/b4770d30-2797-0135-63f7-5ee2fc56bc0c/master.svg)](https://codeship.com/projects/222984)
[![Coverage Status](https://coveralls.io/repos/github/julianghionoiu/tdl-auth/badge.svg?branch=master)](https://coveralls.io/github/julianghionoiu/tdl-auth?branch=master)

## The Auth flow

### 1. Using a private key you securely generate and sign a user token

Token contains:
- unique username
- expiration
- other application specific data
    
### 2. User receives token via mail
 
A URL will be generated with the token contained in the http GET parameters.
The URL will take the user to a webpage with instruction on how to use generate and use the credentials.

### 3. User exchanges token for temporary credentials.

When user clicks the download button a request will be send to the AWS Lambda responsible for generating temporary credentials.

The Lambda does the following:
  - Verifies request signature using public key
  - Generated temporary credentials using STS
  - Creates the credentials files
  - The Lambda will sit behind API Gateway and will be accessed via Http
  
### 4. User will be instructed to save the credentials file

Normally this file will be saved in a location accessible by the consumer applications

## Usage

### Build and run as command-line app
```bash
./gradlew shadowJar
java -Dlogback.configurationFile=`pwd`/logback.xml  \
    -jar ./build/libs/tld-auth-0.0.1-all.jar \
    --region eu-west-2 \
    --bucket tdl-test \
    --username tdl-test-fed01 \
    --file ./build/aws-test-secrets
    
cat ./build/aws-test-secrets | pbcopy
```

### Deploy

Prepare parameters
```bash
cp ./etc/parameters.private.json ./etc/parameters.json 
```

Create Stack
```bash
ENV_DEPLOY_S3_PATH=s3://tdl-artifacts/ \
AWS_S3_CONFIG="--region eu-west-2" \
STACK_NAME=testing-tdl-auth \
./deploy.sh
```

Test
```bash
curl -XPOST https://jjz08ve2q3.execute-api.eu-west-2.amazonaws.com/production/verify --data '{"data":"SGVsbG8gV29ybGQh"}'
```


## Development

The development of this feature will be done incrementally, using short iterations.


**Phase 1** Prove that we can generate temporary credentials

- [DONE] Generate temporary credentials by hand
- [DONE] Copy credentials file to `record-and-upload`
- [DONE] Run `record-and-upload`, the upload should just work

**Phase 2** Ensure that the credentials are valid and secure

- [DONE] The credentials should restrict the users to their personal folder, we need proper tests to prove this
- [DONE] The `record-and-upload` should validate that the user provided has enough permissions to proceed with the recording

**Phase 3** Deployment scripts

- [DONE] Cloud Formation templates for the required infrastructure
- [DONE] Script to package and deploy Lambda

**Phase 4** Secure endpoint

- Script to generate and sign token with private key
- AWS Lambda verifies user token

**Phase 5** User experience

- Prepared URL with token as parameter
- Web front-end with a button to download credentials


### Useful commands


```bash
aws s3api --no-verify-ssl --profile federated --region eu-west-2 list-objects --bucket tdl-test  --prefix tdl-test-fed01
```
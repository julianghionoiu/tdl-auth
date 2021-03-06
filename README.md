[![Java Version](http://img.shields.io/badge/Java-1.8-blue.svg)](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)
[![Codeship Status for julianghionoiu/tdl-auth](https://img.shields.io/codeship/b4770d30-2797-0135-63f7-5ee2fc56bc0c/master.svg)](https://codeship.com/projects/222984)
[![Coverage Status](https://coveralls.io/repos/github/julianghionoiu/tdl-auth/badge.svg?branch=master)](https://coveralls.io/github/julianghionoiu/tdl-auth?branch=master)

# The Auth flow

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
./gradlew clean shadowJar -i
java -Dlogback.configurationFile=`pwd`/logback.xml  \
    -jar ./build/libs/tdl-auth-0.0.1-all.jar \
    --region eu-west-2 \
    --bucket tdl-test \
    --scope test \
    --username fed01 \
    --challenge HLX \
    --file ./build/aws-test-secrets
    
cat ./build/aws-test-secrets | pbcopy
```

### Testing

Steps to test.
1. Execute script `prepare-test.sh`, after previously set environment variable `AWS_CONFIG` and `STACK_NAME` for generating AWS test environment using CloudFormation.
2. Execute gradle `test` action.

### Visual Web Test

Dependencies:
- Docker
- ImageMagick (convert)

To run the web tests:
```
./gradlew webTests -i
```

### Deploy

Prepare resources
To make sure the link generator works properly, you have to do these steps
1. Make sure lambda role can send SES.
2. Make sure sender email is verified. The guide can be read [here](http://docs.aws.amazon.com/ses/latest/DeveloperGuide/verify-email-addresses.html).
3. Make sure S3 bucket is writable by lambda role.

Prepare parameters
```bash
mkdir -p ./configs/deploy
cp ./etc/parameters-template.json ./configs/deploy/parameters.json
cp ./etc/env-template.json ./configs/deploy/env.sh
```

Set the correct parameters in `env.sh` and `parameters.json`. Example:
```bash
ENV_DEPLOY_S3_PATH=s3://tdl-artifacts/ 
AWS_S3_CONFIG="--region eu-west-2" 
STACK_NAME=testing-tdl-auth 
```

Deploy
```bash
./gradlew clean build
./deploy.sh testing
```

Test credentials endpoint
```bash
curl -XPOST https://w62n5pnu7k.execute-api.eu-west-2.amazonaws.com/production/verify --data '{"username": "X", "token":"SGVsbG8gV29ybGQh"}'
```

*Test link generator lambda [TESTING]*
```bash
aws lambda invoke \
--invocation-type RequestResponse \
--function-name tdl-testing-generate-intro \
--region eu-west-2 \
--log-type Tail \
--payload '{"headerImageName": "mountain.jpg",
            "mainChallengeTitle":"Secret",
            "sponsorName": "Julian",
            "inspiredByLabel": "real business domain",
            "codingDurationLabel": "1 hour",
            "defaultLanguage": "Java",
            "videoRecordingOption": "OPTIONAL",
            "enableApplyPressure": true,
            "enableReportSharing": true,
            "username":"mani0310",
            "validityDays": 14, 
            "warmupChallenges": [ "SUM" ],
            "officialChallenge": "HLO"}' \
./build/outputfile.txt 
cat ./build/outputfile.txt
cat ./build/outputfile.txt | tr -d "\"" | sed  's/https/http/' | pbcopy
```

*[LIVE] Hello link generator lambda [LIVE]*
```bash
aws lambda invoke \
--invocation-type RequestResponse \
--function-name tdl-generate-intro \
--region eu-west-2 \
--log-type Tail \
--payload '{"headerImageName": "mountain.jpg",
            "mainChallengeTitle":"Checkout",
            "sponsorName": "Julian",
            "inspiredByLabel": "real business domain",
            "codingDurationLabel": "10 minutes",
            "defaultLanguage": "Java",
            "videoRecordingOption": "OPTIONAL",
            "enableApplyPressure": true,
            "enableReportSharing": true,
            "username":"iuli0831",
            "validityDays": 10,
            "warmupChallenges": [ "SUM" ],
            "officialChallenge": "HLO"}' \
./build/outputfile.txt
cat ./build/outputfile.txt
cat ./build/outputfile.txt | tr -d "\"" | sed  's/https/http/' | pbcopy
```


*Error*
```bash
echo "base64error"  | base64 --decode
```

### Static images - staticResources

Sync test bucket
```
cd staticResources
aws s3 sync . s3://intro.test.befaster.io/000common
```

Sync prod bucket
```
cd staticResources
aws s3 sync . s3://intro.befaster.io/000common
```

### Useful commands


```bash
aws s3api --no-verify-ssl --profile federated --region eu-west-2 list-objects --bucket tdl-test  --prefix tdl-test-fed01
```
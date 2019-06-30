#!/usr/bin/env bash

set -e

if [ $# -eq 0 ]; then echo "Usage: $0 username"; exit 1; fi
USERNAME=$1

PAYLOAD_FILE=$(mktemp)
cat << EOF > ${PAYLOAD_FILE}
{
  "headerImageName": "makers.jpg",
  "mainChallengeTitle": "Developer Insights",
  "sponsorName": "Makers Academy",
  "inspiredByLabel": "real business domain",
  "codingDurationLabel": "3 to 4 hours",
  "defaultLanguage": "Ruby",
  "enableNoVideoOption": false,
  "enableApplyPressure": false,
  "enableReportSharing": true,
  "username":"${USERNAME}",
  "validityDays": 7,
  "warmupChallenges": [ "SUM", "HLO" ],
  "officialChallenge": "CHK"
}
EOF

aws lambda invoke \
--invocation-type RequestResponse \
--function-name tdl-generate-intro \
--region eu-west-2 \
--log-type Tail \
--payload "file://${PAYLOAD_FILE}" \
./build/outputfile.txt
cat ./build/outputfile.txt
cat ./build/outputfile.txt | tr -d "\"" | sed  's/intro.befaster.io.s3.eu-west-2.amazonaws.com/intro.accelerate.io/' | pbcopy

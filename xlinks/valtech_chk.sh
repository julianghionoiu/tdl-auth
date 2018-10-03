#!/usr/bin/env bash

set -e

if [ $# -eq 0 ]; then echo "Usage: $0 username"; exit 1; fi
USERNAME=$1

PAYLOAD_FILE=$(mktemp)
cat << EOF > ${PAYLOAD_FILE}
{
  "headerImageName": "valtech.jpg",
  "mainChallengeTitle": "Insights",
  "sponsorName": "Valtech",
  "codingDurationLabel": "can be solved in 2-3 hours",
  "allowNoVideoOption": false,
  "username":"${USERNAME}",
  "validityDays": 60,
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
cat ./build/outputfile.txt | tr -d "\"" | sed  's/https/http/' | pbcopy
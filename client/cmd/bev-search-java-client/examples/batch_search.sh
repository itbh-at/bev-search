#!/bin/bash

path_prefix="http://localhost:8080"

echo
echo "Please enter the API path prefix without a trailing slash.";
echo " E.g. http://localhost:8080 will become http://localhost:8080/bev-search-rest/v1/at/common/geocode"
echo
echo -n "Path prefix: "
read path_prefix

BASEDIR=$(dirname "$0")
echo "$BASEDIR"
BEV_REST_CLIENT=$BASEDIR/BevRestClient.jar

# Print and run command
function run_command {
    echo $1
    echo
    $1
    return_code=$?
    echo
    echo "Return code: ${return_code}"
}

function check_proceed {
    proceed="n"
    echo
    echo -n "Proceed with the demonstration? [y/n] (Default: y)"
    read proceed
    if [ "$proceed" == "n" ]; then
        exit 0
    fi
    clear
}

clear

echo "--------------------------------------------------------------------------------"
echo "Batch processing an input file with 10 concurrent requests"
echo
echo "Expectation:"
echo " - all fields of the input file are included"
echo " - result fields beginning with an underscore are included"
echo " - return code equals 0"
echo "--------------------------------------------------------------------------------"
echo

check_proceed

run_command "java -jar ${BEV_REST_CLIENT} -r ${path_prefix}/bev-search-rest/v1/at/common/geocode -b ${BASEDIR}/batch_input.csv -t 10"

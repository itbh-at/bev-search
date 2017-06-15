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
echo "Request usage information"
echo
echo "Expectation:"
echo " - usage information is displayed"
echo " - retun code is greater than 0"
echo "--------------------------------------------------------------------------------"
echo

check_proceed

run_command "java -jar ${BEV_REST_CLIENT} --help";

check_proceed

echo "--------------------------------------------------------------------------------"
echo "In the following examples please pay attention to the high"
echo "level of tolerance with respect to bad address inputs."
echo "--------------------------------------------------------------------------------"

check_proceed

echo "--------------------------------------------------------------------------------"
echo "Query for an address where no unique result is available but"
echo "enforce displaying results only if they are unique."
echo
echo "Expectation:"
echo " - no output because there isn't a unique match"
echo " - return code is greater than 0"
echo "--------------------------------------------------------------------------------"
echo

check_proceed

run_command "java -jar ${BEV_REST_CLIENT} -r ${path_prefix}/bev-search-rest/v1/at/common/geocode -a Ortweinstraße  -z 3100 -p 'St. Pölten' -u"

check_proceed

echo "--------------------------------------------------------------------------------"
echo "Query for an address where no unique result is available."
echo "A unique result is not enforced."
echo
echo "Expectation:"
echo " - result is displayed as a semi-colon separated data set"
echo " - return code equals 0"
echo "--------------------------------------------------------------------------------"
echo

check_proceed

run_command "java -jar ${BEV_REST_CLIENT} -r ${path_prefix}/bev-search-rest/v1/at/common/geocode -a Ortweinstraße -z 3100 -p 'St. Pölten'"

check_proceed

echo "--------------------------------------------------------------------------------"
echo "Query for an address where an unique result is enforced and available"
echo
echo "Expectation:"
echo " - result is displayed as a semi-colon separated single line"
echo " - return code equals 0"
echo "--------------------------------------------------------------------------------"
echo

check_proceed

run_command "java -jar ${BEV_REST_CLIENT} -r ${path_prefix}/bev-search-rest/v1/at/common/geocode -a Ortweinstraße -i 15 -z 3100 -p 'St. Pölten'"

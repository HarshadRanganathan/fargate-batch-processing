#!/usr/bin/env bash


usage() {
    echo $'\n'"Usage: $0 --repo {repo}
    Mandatory parameters:
        --repo       {repo}       ECR Repository URI e.g. <account_id>.dkr.ecr.us-east-1.amazonaws.com/s3-batch-processing
    ";
    exit 1
}

if [[ $# -eq 0 ]]; then
    usage
fi

echo "-------------------------------------------------"
while [[ $# -gt 0 ]];
    do
        case "${1}"
        in
        --repo)
            REPO=${2}
            echo "REPO       ${REPO}"
            shift 2
            ;;
        --) shift ; break ;;
        -*|--*=)
            echo "Invalid option: -${1}" >&2
            exit 1
            ;;
        esac
     done
echo "-------------------------------------------------"

if [[ ! "$REPO" ]] ; then
    usage
fi

function loginIntoAwsEcrService {
    echo "Logging into AWS ECR"
    awsLogin=`aws ecr get-login --no-include-email --region us-east-1`
    eval `echo ${awsLogin}`
}

function buildAndPushImage {
    docker build --no-cache --rm=true -t s3-batch-processing .
    docker tag s3-batch-processing:latest ${REPO}:latest
    docker push ${REPO}:latest
}

loginIntoAwsEcrService
buildAndPushImage
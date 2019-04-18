#!/usr/bin/env bash

usage() {
    echo $'\n'"Usage: $0 --cluster {cluster} --task-definition {task-definition} --source {source} --destination {destination}
    Mandatory parameters:
        --cluster           {cluster}           Cluster name e.g. s3-batch-processing
        --task-definition   {task-definition}   Family and revision (family:revision ) or full ARN of the task definition to run
        --source            {source}            S3 source file URL
        --destination       {destination}       S3 Destination URL
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
        --cluster)
            CLUSTER=${2}
            echo "CLUSTER         ${CLUSTER}"
            shift 2
            ;;
        --task-definition)
            TASK_DEFINITION=${2}
            echo "TASK_DEFINITION ${TASK_DEFINITION}"
            shift 2
            ;;
        --source)
            SOURCE=${2}
            echo "SOURCE          ${SOURCE}"
            shift 2
            ;;
        --destination)
            DESTINATION=${2}
            echo "DESTINATION     ${DESTINATION}"
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

if [[ ! "$CLUSTER" ]] || [[ ! "$TASK_DEFINITION" ]] || [[ ! "$SOURCE" ]] || [[ ! "$DESTINATION" ]] ; then
    usage
fi

NETWORK_CONFIGURATION=$(cat <<-EOF
{
  "awsvpcConfiguration": {
    "subnets": ["subnet-e7ab4abe"],
    "assignPublicIp": "DISABLED"
  }
}
EOF
)

OVERRIDES=$(cat <<-EOF
{
  "containerOverrides": [
    {
      "name": "s3-batch-processing",
      "command": ["--source=${SOURCE}", "--destination=${DESTINATION}"]
    }
  ]
}
EOF
)

echo "$(aws ecs run-task --cluster "${CLUSTER}" --task-definition "${TASK_DEFINITION}" --launch-type "FARGATE" --network-configuration "${NETWORK_CONFIGURATION}" --overrides "${OVERRIDES}")"
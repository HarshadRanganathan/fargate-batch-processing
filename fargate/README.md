# Fargate

## Task Definition

Create a new task definition with Fargate launch type using the configuration file `taskDefinition.json`.

Update image and role arn in the file with valid values.

Note: Role should have access to pull images from ECR, push logs to cloudwatch and read/write access to S3.

## Cluster  

Create a new cluster with Fargate template for running our containerized batch tasks.

## Running Task

Execute `run_task.sh` script to run the cluster task in a private subnet (no public IP assigned). 

[Task Networking in AWS Fargate](https://aws.amazon.com/blogs/compute/task-networking-in-aws-fargate/)

```
# e.g. cluster -> s3-batch-processing 
# e.g. task-definition -> s3-batch-processing:1 
./run_task.sh --cluster {cluster} --task-definition {task-definition} --subnet {subnet} --source {source} --destination {destination}
```
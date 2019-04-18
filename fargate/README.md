# Fargate

## Task Definition

Create a new task definition with Fargate launch type using the configuration file `taskDefinition.json`.

Update image and role arn in the file with valid values.

## Cluster  

Create a new cluster with Fargate template for running our containerized batch tasks.

## Running Task

Execute `run_task.sh` script to run a task in the cluster. 

```
# e.g. cluster -> s3-batch-processing 
# e.g. task-definition -> s3-batch-processing:1 
./run_task.sh --cluster {cluster} --task-definition {task-definition} --source {source} --destination {destination}
```
# Fargate Batch Processing

Sample spring batch processing job launched in fargate to copy files in S3. We use step functions to orchestrate the flow.

![Flow](flow.png?raw=true)

# How To Run

[1] Build the batch processing jar by running below command in module `batch`

```
mvn package spring-boot:repackage antrun:run@docker
```

[2] Build the docker image with the artifact and push it to ECR.

[Instructions](docker/README.md)

[3] Create a Fargate cluster to run the containerized task.

[Instructions](fargate/README.md)

[4] Create a state machine in step functions to create an ECS task for S3 file copy using the docker image.

[Instructions](step-functions/README.md)

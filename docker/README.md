# Docker Image

## Local Execution

### Build Image

```
docker build -t s3-batch-processing:latest .
```

### Environment Variables

Add your AWS credentials to `.env` file (don't commit !!) which will be added to the container later to facilitate S3 copy.

### Run Container

Run the container by supplying the environment file and required arguments.

```
docker run --env-file .env s3-batch-processing:latest --source=s3:// --destination=s3://
```

### Run Container (Compose)

Alternatively, you can also run the container via docker compose.

```
docker-compose run s3-batch-processing --source=s3:// --destination=s3://
```

## ECR Push

Create an ECR repository to push the build image.

Run below script by supplying the ECR Repo URI. Ensure you have your AWS credentials set in your system config file.

```
# repo_url e.g. <account_id>.dkr.ecr.us-east-1.amazonaws.com/s3-batch-processing
./ecr.sh --repo <repo_url>
```
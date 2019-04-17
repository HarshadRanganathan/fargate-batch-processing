# Docker Image

## Build Image

```
docker build -t s3-batch-processing:latest .
```

## Environment Variables

Add your AWS credentials to `.env` file (don't commit !!) which will be added to the container later to facilitate S3 copy.

## Run Container

We run the container by supplying the environment file and arguments.

```
docker run --env-file .env s3-batch-processing:latest --source=s3://<url> --destination=s3://<url>
```

## Run Container (Compose)

```
docker-compose run s3-batch-processing --source=s3://<url> --destination=s3://<url>
```
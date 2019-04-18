# Spring Batch Processing

Spring batch job that copies a large file in S3 from source location to destination using Multipart Upload.

Dependencies:
- [Spring Boot](https://spring.io/projects/spring-boot)
- [Spring Batch](https://spring.io/projects/spring-batch)
- [AWS SDK](https://aws.amazon.com/sdk-for-java/)

## Pre-Requisites

This project uses DefaultAWSCredentialsProviderChain which looks for AWS credentials in below order:
 - Environment Variables - AWS_ACCESS_KEY_ID and AWS_SECRET_ACCESS_KEY (RECOMMENDED since they are recognized by all the AWS SDKs and CLI except for .NET), or AWS_ACCESS_KEY and AWS_SECRET_KEY (only recognized by Java SDK)
 - Java System Properties - aws.accessKeyId and aws.secretKey
 - Credential profiles file at the default location (~/.aws/credentials) shared by all AWS SDKs and the AWS CLI
 - Credentials delivered through the Amazon EC2 container service if AWS_CONTAINER_CREDENTIALS_RELATIVE_URI" environment variable is set and security manager has permission to access the variable,
 - Instance profile credentials delivered through the Amazon EC2 metadata service

## Build JAR

Below command builds the jar file and copies it to the docker folder.

```
mvn package spring-boot:repackage antrun:run@docker
```

## Launch Job

Run below command with proper S3 URL for file copy.

```
java -jar s3-batch-processing-1.0-SNAPSHOT.jar --source=s3:// --destination=s3://
```
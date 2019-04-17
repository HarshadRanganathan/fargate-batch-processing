# Spring Batch Processing

Sample Spring Batch Job that copies a large file from source location to destination location using Multipart Upload.

Dependencies:
- [Spring Boot](https://spring.io/projects/spring-boot)
- [Spring Batch](https://spring.io/projects/spring-batch)
- [AWS SDK](https://aws.amazon.com/sdk-for-java/)

Build JAR:

```
mvn package spring-boot:repackage
```

Launch Job:

```
java -jar s3-batch-processing-1.0-SNAPSHOT.jar --source=s3://<url> --destination=s3://<url>
```
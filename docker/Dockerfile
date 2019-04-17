FROM openjdk:8-jre-alpine

WORKDIR /app

COPY build/s3-batch-processing.jar .

ENTRYPOINT ["java", "-jar", "s3-batch-processing.jar"]
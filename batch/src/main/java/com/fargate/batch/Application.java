package com.fargate.batch;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Application
{

    @Bean
    public ClientConfiguration clientConfiguration() {
        return new ClientConfiguration();
    }

    @Bean
    public AWSCredentialsProvider awsCredentialsProvider() {
        return new DefaultAWSCredentialsProviderChain();
    }

    @Bean
    public AmazonS3 amazonS3(ClientConfiguration clientConfiguration, AWSCredentialsProvider awsCredentialsProvider) {
        return AmazonS3ClientBuilder.standard()
                .withClientConfiguration(clientConfiguration)
                .withCredentials(awsCredentialsProvider)
                .build();
    }

    public static void main( String[] args )
    {
        SpringApplication.run(Application.class, args);
    }
}

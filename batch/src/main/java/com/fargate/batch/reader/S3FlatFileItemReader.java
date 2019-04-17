package com.fargate.batch.reader;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3URI;
import com.amazonaws.services.s3.model.GetObjectRequest;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;

import java.io.IOException;
import java.io.InputStream;

public class S3FlatFileItemReader<T> extends FlatFileItemReader<T> implements StepExecutionListener {

    @Autowired
    private AmazonS3 amazonS3;

    @Value("${source}")
    private String source;

    private InputStream inputStream;

    @Override
    public void beforeStep(StepExecution stepExecution) {
        final AmazonS3URI amazonS3URI = new AmazonS3URI(source);
        final GetObjectRequest getObjectRequest = new GetObjectRequest(amazonS3URI.getBucket(), amazonS3URI.getKey());
        this.inputStream = amazonS3.getObject(getObjectRequest).getObjectContent();
        super.setResource(new InputStreamResource(inputStream));
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        if(inputStream != null) {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}

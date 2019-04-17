package com.fargate.batch.writer;

import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3URI;
import com.amazonaws.services.s3.model.*;
import org.springframework.batch.core.ChunkListener;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.item.support.AbstractItemStreamItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class S3FlatFileItemWriter<T> extends AbstractItemStreamItemWriter<T> implements StepExecutionListener, ChunkListener {

    @Autowired
    private AmazonS3 amazonS3;

    @Value("${destination}")
    private String destination;

    private InitiateMultipartUploadResult initiateMultipartUploadResult;

    private List<PartETag> partETags;

    private int partNumber;

    private String s3Bucket;

    private String s3Key;

    private static final long partSize = 5 * 1024 * 1024;

    private static final String DEFAULT_LINE_SEPARATOR = "\n";

    @Override
    public void beforeStep(StepExecution stepExecution) {
        final AmazonS3URI amazonS3URI = new AmazonS3URI(destination);
        this.s3Bucket = amazonS3URI.getBucket();
        this.s3Key = amazonS3URI.getKey();

        final InitiateMultipartUploadRequest initiateMultipartUploadRequest = new InitiateMultipartUploadRequest(this.s3Bucket, this.s3Key);
        this.initiateMultipartUploadResult = amazonS3.initiateMultipartUpload(initiateMultipartUploadRequest);
        this.partETags = new ArrayList<>();
    }

    @Override
    public void beforeChunk(ChunkContext chunkContext) {
        this.partNumber = chunkContext.getStepContext().getStepExecution().getCommitCount() + 1;
    }


    @Override
    public void write(List<? extends T> items) {
        final String content = items.stream().map(item -> item.toString()).collect(Collectors.joining(DEFAULT_LINE_SEPARATOR));
        byte[] contentAsBytes = content.getBytes(StandardCharsets.UTF_8);

        final UploadPartRequest uploadPartRequest = new UploadPartRequest()
                .withBucketName(this.s3Bucket)
                .withKey(this.s3Key)
                .withUploadId(this.initiateMultipartUploadResult.getUploadId())
                .withPartNumber(this.partNumber)
                .withInputStream(new ByteArrayInputStream(contentAsBytes))
                .withPartSize(Math.min(partSize, contentAsBytes.length));
        final UploadPartResult uploadPartResult = amazonS3.uploadPart(uploadPartRequest);
        partETags.add(uploadPartResult.getPartETag());
    }

    @Override
    public void afterChunk(ChunkContext chunkContext) {}

    @Override
    public void afterChunkError(ChunkContext chunkContext) {}

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        try {
            final CompleteMultipartUploadRequest completeMultipartUploadRequest = new CompleteMultipartUploadRequest(this.s3Bucket, this.s3Key, initiateMultipartUploadResult.getUploadId(), partETags);
            amazonS3.completeMultipartUpload(completeMultipartUploadRequest);
        } catch (SdkClientException ex) {
            final AbortMultipartUploadRequest abortMultipartUploadRequest = new AbortMultipartUploadRequest(this.s3Bucket, this.s3Key, initiateMultipartUploadResult.getUploadId());
            amazonS3.abortMultipartUpload(abortMultipartUploadRequest);
        }
        return null;
    }
}

package com.fargate.batch.configuration;

import com.fargate.batch.reader.S3FlatFileItemReader;
import com.fargate.batch.writer.S3FlatFileItemWriter;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.file.FlatFileParseException;
import org.springframework.batch.item.file.mapping.PassThroughLineMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Bean
    @StepScope
    public S3FlatFileItemReader<String> s3FlatFileItemReader() {
        final S3FlatFileItemReader<String> s3FlatFileItemReader = new S3FlatFileItemReader<>();
        s3FlatFileItemReader.setName("s3ItemReader");
        s3FlatFileItemReader.setLineMapper(new PassThroughLineMapper());
        return s3FlatFileItemReader;
    }

    @Bean
    @StepScope
    public <T> S3FlatFileItemWriter<T> s3FlatFileItemWriter() {
        return new S3FlatFileItemWriter<>();
    }

    @Bean
    public Job s3Job(Step processS3File) {
        return jobBuilderFactory.get("s3Job")
                .incrementer(new RunIdIncrementer())
                .flow(processS3File)
                .end()
                .build();
    }

    @Bean
    public Step processS3File(S3FlatFileItemReader<String> s3FlatFileItemReader,
                              S3FlatFileItemWriter<Object> s3FlatFileItemWriter) {
        return stepBuilderFactory.get("processS3File")
                .chunk(1000)
                .reader(s3FlatFileItemReader)
                .writer(s3FlatFileItemWriter)
                .faultTolerant()
                .skip(FlatFileParseException.class)
                .build();
    }
}

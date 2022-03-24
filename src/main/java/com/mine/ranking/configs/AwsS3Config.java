package com.mine.ranking.configs;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.TransferManagerBuilder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration containing the beans needed by the amazon S3 client.
 */
@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "aws.s3")
public class AwsS3Config
{

    /**
     * Identifies the region of the S3.
     */
    private String region;

    /**
     * Identifies the name of the S3 bucket.
     */
    private String bucketName;

    /**
     * Identifies the name of the file in S3 bucket.
     */
    private String fileName;

    /**
     * Identifies the key in S3 bucket.
     */
    private String key;

    @Bean
    public TransferManager transferManager(AmazonS3 amazonS3Client)
    {
        return TransferManagerBuilder
            .standard()
            .withS3Client(amazonS3Client)
            .build();
    }

    @Bean
    public AmazonS3 amazonS3Client()
    {
        return AmazonS3ClientBuilder
            .standard()
            .withRegion(Regions.fromName(region))
            .build();
    }
}

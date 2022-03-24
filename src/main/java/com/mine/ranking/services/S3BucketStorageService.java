package com.mine.ranking.services;

import com.amazonaws.event.ProgressEventType;
import com.amazonaws.event.ProgressListener;
import com.amazonaws.services.s3.transfer.Download;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.mine.ranking.configs.AwsS3Config;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Path;

/**
 * Provides the necessary functions to download a file from S3, particularly from the bucket specified in the configuration.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class S3BucketStorageService
{

    private final TransferManager transferManager;

    private final AwsS3Config awsS3Config;

    /**
     * Downloads the file from S3 bucket.
     *
     * @return the file downloaded from s3.
     */
    public Path downloadFile()
    {
        final String fileName = awsS3Config.getFileName();

        File file = new File(fileName);

        Download download = transferManager.download(awsS3Config.getBucketName(), awsS3Config.getKey(), file);
        download.addProgressListener((ProgressListener) progressEvent ->
        {
            final ProgressEventType event = progressEvent.getEventType();
            switch (event)
            {
                case TRANSFER_STARTED_EVENT -> log.debug("File {} download started", fileName);
                case TRANSFER_COMPLETED_EVENT -> log.info("File {} downloaded successfully. {}", fileName, progressEvent);
                case TRANSFER_CANCELED_EVENT -> log.warn("File {} download cancelled. {}", fileName, progressEvent);
                case TRANSFER_FAILED_EVENT -> log.error("File {} download failed. {}", fileName, progressEvent);
                default -> log.debug("{}", progressEvent);
            }
        });

        // Waits until the download from s3 is finished.
        try
        {
            download.waitForCompletion();
        }
        catch (InterruptedException e)
        {
            throw new IllegalStateException(
                String.format("File transfer %s interrupted. %s", fileName, e.getMessage()));
        }

        return file.toPath();
    }
}

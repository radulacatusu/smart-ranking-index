package com.mine.ranking.services;

import com.amazonaws.event.ProgressListenerChain;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.TransferProgress;
import com.amazonaws.services.s3.transfer.internal.DownloadImpl;
import com.amazonaws.services.s3.transfer.internal.DownloadMonitor;
import com.amazonaws.services.s3.transfer.internal.TransferStateChangeListener;
import com.mine.ranking.configs.AwsS3Config;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.File;
import java.nio.file.Path;
import java.util.Date;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest({TransferProgress.class})
public class S3BucketStorageServiceTest
{

    private static final String DOWNLOAD_DESCRIPTION = "downloadDescription";

    private static final String BUCKET_NAME = "testBucket";

    private static final String FILE_NAME = "testFileName";

    private static final String KEY = "public/testFileName";

    private TransferManager transferManager;

    private Future<File> future;

    private S3BucketStorageService s3BucketStorageService;

    @Before
    public void init()
    {
        future = mock(Future.class);
        transferManager = mock(TransferManager.class);

        DownloadImpl download = generateDownload(future);
        when(transferManager.download(eq(BUCKET_NAME), eq(KEY), any(File.class))).thenReturn(download);

        s3BucketStorageService = new S3BucketStorageService(transferManager, initializeAwsS3Config());
    }

    @Test
    public void happyPath() throws ExecutionException, InterruptedException
    {
        when(future.get()).thenReturn(new File(FILE_NAME));

        Path path = s3BucketStorageService.downloadFile();
        verify(transferManager).download(eq(BUCKET_NAME), eq(KEY), any(File.class));

        assertNotNull(path);
        assertEquals(FILE_NAME, path.toString());
    }

    @Test(expected = IllegalStateException.class)
    public void waitForCompletionFails() throws ExecutionException, InterruptedException
    {
        doThrow(new InterruptedException()).when(future).get();

        s3BucketStorageService.downloadFile();

        verify(transferManager).download(eq(BUCKET_NAME), eq(KEY), any(File.class));
    }

    private DownloadImpl generateDownload(Future<File> future)
    {
        TransferProgress transferProgress = mock(TransferProgress.class);
        ProgressListenerChain progressListenerChain = mock(ProgressListenerChain.class);
        TransferStateChangeListener transferStateChangeListener = mock(TransferStateChangeListener.class);
        GetObjectRequest getObjectRequest = mock(GetObjectRequest.class);
        ObjectMetadata objectMetadata = mock(ObjectMetadata.class);
        S3Object s3Object = mock(S3Object.class);

        DownloadMonitor downloadMonitor = mock(DownloadMonitor.class);
        when(downloadMonitor.isDone()).thenReturn(true);
        when(downloadMonitor.getFuture()).thenReturn(future);

        when(objectMetadata.getLastModified()).thenReturn(new Date());
        when(s3Object.getObjectMetadata()).thenReturn(objectMetadata);

        DownloadImpl download = new DownloadImpl(DOWNLOAD_DESCRIPTION, transferProgress, progressListenerChain, s3Object,
                                                 transferStateChangeListener, getObjectRequest, new File(FILE_NAME), objectMetadata, false);
        download.setMonitor(downloadMonitor);

        return download;
    }

    private AwsS3Config initializeAwsS3Config()
    {
        AwsS3Config awsS3Config = new AwsS3Config();
        awsS3Config.setBucketName(BUCKET_NAME);
        awsS3Config.setFileName(FILE_NAME);
        awsS3Config.setKey(KEY);
        return awsS3Config;
    }
}
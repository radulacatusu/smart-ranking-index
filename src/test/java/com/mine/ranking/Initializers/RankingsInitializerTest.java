package com.mine.ranking.Initializers;

import com.mine.ranking.dtos.Ranking;
import com.mine.ranking.services.S3BucketStorageService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
public class RankingsInitializerTest
{

    private S3BucketStorageService s3BucketStorageService;

    private RankingsInitializer rankingsInitializer;

    @Before
    public void init() throws IOException
    {
        final File testFile = new ClassPathResource("test-case-keywords.csv").getFile();

        s3BucketStorageService = mock(S3BucketStorageService.class);
        when(s3BucketStorageService.downloadFile()).thenReturn(testFile.toPath());

        rankingsInitializer = new RankingsInitializer(s3BucketStorageService);
    }

    @Test
    public void downloadFile()
    {
        rankingsInitializer.downloadFile();
        verify(s3BucketStorageService).downloadFile();

        List<Ranking> rankings = rankingsInitializer.getRankings();

        assertNotNull(rankings);
        assertEquals(2, rankings.size());

        assertEquals(
            Ranking
                .builder()
                .timestamp(ZonedDateTime.ofInstant(Instant.ofEpochSecond(1637024931L), ZoneOffset.UTC))
                .asin("B092SS35LK")
                .keyword("2012 f250 wheel hub")
                .rank(90)
                .build(),
            rankings.get(0));
        assertEquals(
            Ranking
                .builder()
                .timestamp(ZonedDateTime.ofInstant(Instant.ofEpochSecond(1637024931L), ZoneOffset.UTC))
                .asin("B092SS2ZND")
                .keyword("2012 f250 wheel hub")
                .rank(91)
                .build(),
            rankings.get(1));
    }
}
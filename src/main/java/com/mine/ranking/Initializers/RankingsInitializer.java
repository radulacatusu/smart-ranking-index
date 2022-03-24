package com.mine.ranking.Initializers;

import com.mine.ranking.dtos.Ranking;
import com.mine.ranking.services.S3BucketStorageService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Initializes the ranking list by downloading the rankings from s3.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class RankingsInitializer
{

    @Getter
    private final List<Ranking> rankings = new ArrayList<>();

    private final S3BucketStorageService s3BucketStorageService;

    /**
     * Receives the {@link ApplicationReadyEvent} event from Spring as soon as the application context has fully loaded.
     * This ensures that we can safely download the file from S3 and store all the ranking in a list.
     */
    @EventListener(value = ApplicationReadyEvent.class,
        condition = "@environment.getActiveProfiles().length == 0 || "
                    + "(@environment.getActiveProfiles().length > 0 && @environment.getActiveProfiles()[0] != 'test')")
    public void downloadFile()
    {
        Path filePath = s3BucketStorageService.downloadFile();

        try
        {
            Files
                .lines(filePath)
                //Skip the header
                .skip(1)
                .map(line -> line.split(";"))
                .forEach(line -> rankings.add(
                    new Ranking(
                        ZonedDateTime.ofInstant(Instant.ofEpochSecond(Long.parseLong(line[0])), ZoneOffset.UTC),
                        line[1],
                        line[2],
                        Integer.valueOf(StringUtils.trimTrailingCharacter(line[3], ',')))));

            log.info("Parsing of the file has finished.");
        }
        catch (IOException e)
        {
            log.error("The file couldn't be opened");
        }
    }
}

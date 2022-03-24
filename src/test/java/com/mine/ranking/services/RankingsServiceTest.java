package com.mine.ranking.services;

import com.mine.ranking.Initializers.RankingsInitializer;
import com.mine.ranking.dtos.Ranking;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

//TODO Write the test class in groovy with spock for better visibility.
@RunWith(SpringRunner.class)
public class RankingsServiceTest
{

    private static final long TIMESTAMP_1 = 1637024931L;

    private static final long TIMESTAMP_2 = 1638024931L;

    private static final String KEYWORD_1 = "2012 f250 wheel hub";

    private static final String KEYWORD_2 = "2014 f250 wheel hub";

    private static final String ASIN_B092SS2ZND = "B092SS2ZND";

    private static final String ASIN_B092SS35LK = "B092SS35LK";

    private RankingsInitializer rankingsInitializer;

    private RankingsService rankingsService;

    @Before
    public void init()
    {
        final List<Ranking> allRankings = initializeRankings();

        rankingsInitializer = mock(RankingsInitializer.class);
        when(rankingsInitializer.getRankings()).thenReturn(allRankings);

        rankingsService = new RankingsService(rankingsInitializer);
    }

    @Test
    public void calculateIndividualRankingsByKeyword()
    {
        List<Ranking> rankings = rankingsService.calculateIndividualRankings(KEYWORD_1);
        verifyGetAllRankings();

        assertEquals(6, rankings.size());

        assertEquals(
            Ranking
                .builder()
                .timestamp(ZonedDateTime.ofInstant(Instant.ofEpochSecond(TIMESTAMP_1), ZoneOffset.UTC))
                .asin(ASIN_B092SS2ZND)
                .keyword(KEYWORD_1)
                .rank(91)
                .build(),
            rankings.get(0));
        assertEquals(
            Ranking
                .builder()
                .timestamp(ZonedDateTime.ofInstant(Instant.ofEpochSecond(TIMESTAMP_1), ZoneOffset.UTC))
                .asin(ASIN_B092SS35LK)
                .keyword(KEYWORD_1)
                .rank(90)
                .build(),
            rankings.get(1));
        assertEquals(
            Ranking
                .builder()
                .timestamp(ZonedDateTime.ofInstant(Instant.ofEpochSecond(TIMESTAMP_1), ZoneOffset.UTC))
                .asin(ASIN_B092SS35LK)
                .keyword(KEYWORD_1)
                .rank(100)
                .build(),
            rankings.get(2));
        assertEquals(
            Ranking
                .builder()
                .timestamp(ZonedDateTime.ofInstant(Instant.ofEpochSecond(TIMESTAMP_2), ZoneOffset.UTC))
                .asin(ASIN_B092SS2ZND)
                .keyword(KEYWORD_1)
                .rank(44)
                .build(),
            rankings.get(3));
        assertEquals(
            Ranking
                .builder()
                .timestamp(ZonedDateTime.ofInstant(Instant.ofEpochSecond(TIMESTAMP_2), ZoneOffset.UTC))
                .asin(ASIN_B092SS35LK)
                .keyword(KEYWORD_1)
                .rank(11)
                .build(),
            rankings.get(4));
        assertEquals(
            Ranking
                .builder()
                .timestamp(ZonedDateTime.ofInstant(Instant.ofEpochSecond(TIMESTAMP_2), ZoneOffset.UTC))
                .asin(ASIN_B092SS35LK)
                .keyword(KEYWORD_1)
                .rank(22)
                .build(),
            rankings.get(5));
    }

    @Test
    public void calculateIndividualRankingsByKeywordAndAsin()
    {
        List<Ranking> rankings = rankingsService.calculateIndividualRankings(KEYWORD_1, ASIN_B092SS35LK);
        verifyGetAllRankings();

        assertEquals(4, rankings.size());
        assertEquals(
            Ranking
                .builder()
                .timestamp(ZonedDateTime.ofInstant(Instant.ofEpochSecond(TIMESTAMP_1), ZoneOffset.UTC))
                .asin(ASIN_B092SS35LK)
                .keyword(KEYWORD_1)
                .rank(90)
                .build(),
            rankings.get(0));
        assertEquals(
            Ranking
                .builder()
                .timestamp(ZonedDateTime.ofInstant(Instant.ofEpochSecond(TIMESTAMP_1), ZoneOffset.UTC))
                .asin(ASIN_B092SS35LK)
                .keyword(KEYWORD_1)
                .rank(100)
                .build(),
            rankings.get(1));
        assertEquals(
            Ranking
                .builder()
                .timestamp(ZonedDateTime.ofInstant(Instant.ofEpochSecond(TIMESTAMP_2), ZoneOffset.UTC))
                .asin(ASIN_B092SS35LK)
                .keyword(KEYWORD_1)
                .rank(11)
                .build(),
            rankings.get(2));
        assertEquals(
            Ranking
                .builder()
                .timestamp(ZonedDateTime.ofInstant(Instant.ofEpochSecond(TIMESTAMP_2), ZoneOffset.UTC))
                .asin(ASIN_B092SS35LK)
                .keyword(KEYWORD_1)
                .rank(22)
                .build(),
            rankings.get(3));
    }

    @Test
    public void calculateAggregatedRanksPerAsin()
    {
        List<Ranking> rankings = rankingsService.calculateAggregatedRanksPerAsin(KEYWORD_1);
        verifyGetAllRankings();

        assertEquals(4, rankings.size());

        assertEquals(
            Ranking
                .builder()
                .timestamp(ZonedDateTime.ofInstant(Instant.ofEpochSecond(TIMESTAMP_1), ZoneOffset.UTC))
                .asin(ASIN_B092SS35LK)
                .rank(190)
                .build(),
            rankings.get(0));
        assertEquals(
            Ranking
                .builder()
                .timestamp(ZonedDateTime.ofInstant(Instant.ofEpochSecond(TIMESTAMP_1), ZoneOffset.UTC))
                .asin(ASIN_B092SS2ZND)
                .rank(91)
                .build(),
            rankings.get(1));
        assertEquals(
            Ranking
                .builder()
                .timestamp(ZonedDateTime.ofInstant(Instant.ofEpochSecond(TIMESTAMP_2), ZoneOffset.UTC))
                .asin(ASIN_B092SS35LK)
                .rank(33)
                .build(),
            rankings.get(2));
        assertEquals(
            Ranking
                .builder()
                .timestamp(ZonedDateTime.ofInstant(Instant.ofEpochSecond(TIMESTAMP_2), ZoneOffset.UTC))
                .asin(ASIN_B092SS2ZND)
                .rank(44)
                .build(),
            rankings.get(3));
    }

    @Test
    public void calculateAggregatedRanksForAllAsins()
    {
        List<Ranking> rankings = rankingsService.calculateAggregatedRanksForAllAsins(KEYWORD_1);
        verifyGetAllRankings();

        assertEquals(2, rankings.size());

        assertEquals(
            Ranking
                .builder()
                .timestamp(ZonedDateTime.ofInstant(Instant.ofEpochSecond(TIMESTAMP_1), ZoneOffset.UTC))
                .rank(281)
                .build(),
            rankings.get(0));
        assertEquals(
            Ranking
                .builder()
                .timestamp(ZonedDateTime.ofInstant(Instant.ofEpochSecond(TIMESTAMP_2), ZoneOffset.UTC))
                .rank(77)
                .build(),
            rankings.get(1));
    }

    @Test
    public void calculateAggregatedRanksPerKeyword()
    {
        List<Ranking> rankings = rankingsService.calculateAggregatedRanksPerKeyword(ASIN_B092SS35LK);
        verifyGetAllRankings();

        assertEquals(4, rankings.size());

        assertEquals(
            Ranking
                .builder()
                .timestamp(ZonedDateTime.ofInstant(Instant.ofEpochSecond(TIMESTAMP_1), ZoneOffset.UTC))
                .keyword(KEYWORD_2)
                .rank(15)
                .build(),
            rankings.get(0));
        assertEquals(
            Ranking
                .builder()
                .timestamp(ZonedDateTime.ofInstant(Instant.ofEpochSecond(TIMESTAMP_1), ZoneOffset.UTC))
                .keyword(KEYWORD_1)
                .rank(190)
                .build(),
            rankings.get(1));
        assertEquals(
            Ranking
                .builder()
                .timestamp(ZonedDateTime.ofInstant(Instant.ofEpochSecond(TIMESTAMP_2), ZoneOffset.UTC))
                .keyword(KEYWORD_2)
                .rank(33)
                .build(),
            rankings.get(2));
        assertEquals(
            Ranking
                .builder()
                .timestamp(ZonedDateTime.ofInstant(Instant.ofEpochSecond(TIMESTAMP_2), ZoneOffset.UTC))
                .keyword(KEYWORD_1)
                .rank(33)
                .build(),
            rankings.get(3));
    }

    @Test
    public void calculateAggregatedRanksForAllKeywords()
    {
        List<Ranking> rankings = rankingsService.calculateAggregatedRanksForAllKeywords(ASIN_B092SS35LK);
        verifyGetAllRankings();

        assertEquals(2, rankings.size());
        assertEquals(
            Ranking
                .builder()
                .timestamp(ZonedDateTime.ofInstant(Instant.ofEpochSecond(TIMESTAMP_1), ZoneOffset.UTC))
                .rank(205)
                .build(),
            rankings.get(0));
        assertEquals(
            Ranking
                .builder()
                .timestamp(ZonedDateTime.ofInstant(Instant.ofEpochSecond(TIMESTAMP_2), ZoneOffset.UTC))
                .rank(66)
                .build(),
            rankings.get(1));
    }

    private List<Ranking> initializeRankings()
    {
        return List.of(
            Ranking.builder()
                   .timestamp(ZonedDateTime.ofInstant(Instant.ofEpochSecond(TIMESTAMP_1), ZoneOffset.UTC))
                   .asin(ASIN_B092SS35LK)
                   .keyword(KEYWORD_1)
                   .rank(90)
                   .build(),
            Ranking.builder()
                   .timestamp(ZonedDateTime.ofInstant(Instant.ofEpochSecond(TIMESTAMP_1), ZoneOffset.UTC))
                   .asin(ASIN_B092SS35LK)
                   .keyword(KEYWORD_1)
                   .rank(100)
                   .build(),
            Ranking.builder()
                   .timestamp(ZonedDateTime.ofInstant(Instant.ofEpochSecond(TIMESTAMP_1), ZoneOffset.UTC))
                   .asin(ASIN_B092SS35LK)
                   .keyword(KEYWORD_2)
                   .rank(15)
                   .build(),
            Ranking.builder()
                   .timestamp(ZonedDateTime.ofInstant(Instant.ofEpochSecond(TIMESTAMP_1), ZoneOffset.UTC))
                   .asin(ASIN_B092SS2ZND)
                   .keyword(KEYWORD_1)
                   .rank(91)
                   .build(),
            Ranking.builder()
                   .timestamp(ZonedDateTime.ofInstant(Instant.ofEpochSecond(TIMESTAMP_2), ZoneOffset.UTC))
                   .asin(ASIN_B092SS35LK)
                   .keyword(KEYWORD_1)
                   .rank(11)
                   .build(),
            Ranking.builder()
                   .timestamp(ZonedDateTime.ofInstant(Instant.ofEpochSecond(TIMESTAMP_2), ZoneOffset.UTC))
                   .asin(ASIN_B092SS35LK)
                   .keyword(KEYWORD_1)
                   .rank(22)
                   .build(),
            Ranking.builder()
                   .timestamp(ZonedDateTime.ofInstant(Instant.ofEpochSecond(TIMESTAMP_2), ZoneOffset.UTC))
                   .asin(ASIN_B092SS35LK)
                   .keyword(KEYWORD_2)
                   .rank(33)
                   .build(),
            Ranking.builder()
                   .timestamp(ZonedDateTime.ofInstant(Instant.ofEpochSecond(TIMESTAMP_2), ZoneOffset.UTC))
                   .asin(ASIN_B092SS2ZND)
                   .keyword(KEYWORD_1)
                   .rank(44)
                   .build());
    }

    public void verifyGetAllRankings()
    {
        verify(rankingsInitializer).getRankings();
    }
}
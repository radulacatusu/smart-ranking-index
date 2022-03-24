package com.mine.ranking.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mine.ranking.dtos.Ranking;
import com.mine.ranking.dtos.RankingResponse;
import com.mine.ranking.services.RankingsService;
import org.junit.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

import java.io.UnsupportedEncodingException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.powermock.api.mockito.PowerMockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class RankingControllerTest extends MockMvcBase
{

    public static final String ASIN_B092SS2ZND = "B092SS2ZND";

    private static final String KEYWORD_1 = "2012 f250 wheel hub";

    private final Ranking ranking = Ranking.builder()
                                           .timestamp(ZonedDateTime.ofInstant(Instant.ofEpochSecond(1637024931L), ZoneId.of("UTC")))
                                           .asin(ASIN_B092SS2ZND)
                                           .keyword(KEYWORD_1)
                                           .rank(90)
                                           .build();

    private final List<Ranking> rankings = List.of(ranking);

    @MockBean
    private RankingsService rankingsService;

    @Test
    public void getRanksByKeyword() throws Exception
    {
        when(rankingsService.calculateIndividualRankings(KEYWORD_1)).thenReturn(rankings);

        MvcResult result = this.mockMvc.perform(
                                   get("/smart-ranking-index/ranks/" + KEYWORD_1)
                                       .contentType(MediaType.APPLICATION_JSON))
                                       .andExpect(status().isOk())
                                       .andReturn();

        verifyResult(result);
    }

    @Test
    public void getRanksByKeywordAndAsin() throws Exception
    {
        when(rankingsService.calculateIndividualRankings(KEYWORD_1, ASIN_B092SS2ZND)).thenReturn(rankings);

        MvcResult result = this.mockMvc.perform(
                                   get("/smart-ranking-index/ranks/" + KEYWORD_1 + "/" + ASIN_B092SS2ZND)
                                       .contentType(MediaType.APPLICATION_JSON))
                                       .andExpect(status().isOk())
                                       .andReturn();

        verifyResult(result);
    }

    @Test
    public void getAggregatedRanksPerAsin() throws Exception
    {
        when(rankingsService.calculateAggregatedRanksPerAsin(KEYWORD_1)).thenReturn(rankings);

        MvcResult result = this.mockMvc.perform(
                                   get("/smart-ranking-index/detailed-keyword-ranks/" + KEYWORD_1)
                                       .contentType(MediaType.APPLICATION_JSON))
                                       .andExpect(status().isOk())
                                       .andReturn();

        verifyResult(result);
    }

    @Test
    public void getAggregatedRanksForAllAsins() throws Exception
    {
        when(rankingsService.calculateAggregatedRanksForAllAsins(KEYWORD_1)).thenReturn(rankings);

        MvcResult result = this.mockMvc.perform(
                                   get("/smart-ranking-index/keyword-ranks/" + KEYWORD_1)
                                       .contentType(MediaType.APPLICATION_JSON))
                                       .andExpect(status().isOk())
                                       .andReturn();

        verifyResult(result);
    }

    @Test
    public void getAggregatedRanksPerKeyword() throws Exception
    {
        when(rankingsService.calculateAggregatedRanksPerKeyword(ASIN_B092SS2ZND)).thenReturn(rankings);

        MvcResult result = this.mockMvc.perform(
                                   get("/smart-ranking-index/detailed-asin-ranks/" + ASIN_B092SS2ZND)
                                       .contentType(MediaType.APPLICATION_JSON))
                                       .andExpect(status().isOk())
                                       .andReturn();

        verifyResult(result);
    }

    @Test
    public void getAggregatedRanksForAllKeywords() throws Exception
    {
        when(rankingsService.calculateAggregatedRanksForAllKeywords(ASIN_B092SS2ZND)).thenReturn(rankings);

        MvcResult result = this.mockMvc.perform(
                                   get("/smart-ranking-index/asin-ranks/" + ASIN_B092SS2ZND)
                                       .contentType(MediaType.APPLICATION_JSON))
                                       .andExpect(status().isOk())
                                       .andReturn();

        verifyResult(result);
    }

    private void verifyResult(MvcResult result) throws JsonProcessingException, UnsupportedEncodingException
    {
        RankingResponse rankingResponse = objectMapper.readValue(result.getResponse().getContentAsString(), RankingResponse.class);
        assertNotNull(rankingResponse);
        assertNotNull(rankingResponse.getRankings());
        assertEquals(1, rankingResponse.getRankings().size());
        assertEquals(ranking, rankingResponse.getRankings().get(0));
    }
}
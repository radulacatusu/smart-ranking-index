package com.mine.ranking.controllers;

import com.mine.ranking.dtos.Ranking;
import com.mine.ranking.dtos.RankingResponse;
import com.mine.ranking.services.RankingsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Exposes endpoints to retrieve the rankings.
 */
@RestController
@RequestMapping("/smart-ranking-index")
@RequiredArgsConstructor
public class RankingController
{

    private final RankingsService rankingsService;

    /**
     * Retrieves a time series containing the individual ranks for an ASIN, for a certain keyword.
     *
     * @param keyword the key word to look for.
     *
     * @return a {@link RankingResponse} containing the list of rankings.
     */
    @GetMapping("/ranks/{keyword}")
    @ResponseBody
    RankingResponse getRanksByKeyword(@PathVariable String keyword)
    {
        List<Ranking> rankings = rankingsService.calculateIndividualRankings(keyword);
        return new RankingResponse(rankings);
    }

    /**
     * Retrieves a time series containing the individual ranks of a certain ASIN, for a certain keyword.
     *
     * @param keyword the key word to look for.
     * @param asin the asin to look for.
     *
     * @return a {@link RankingResponse} containing the list of rankings.
     */
    @GetMapping("/ranks/{keyword}/{asin}")
    @ResponseBody
    RankingResponse getRanksByKeywordAndAsin(@PathVariable String keyword, @PathVariable String asin)
    {
        List<Ranking> rankings = rankingsService.calculateIndividualRankings(keyword, asin);
        return new RankingResponse(rankings);
    }

    /**
     * Retrieves a time series containing the aggregated ranks per ASIN for a certain keyword.
     *
     * @param keyword the key word to look for.
     *
     * @return a {@link RankingResponse} containing the list of rankings.
     */
    @GetMapping("/detailed-keyword-ranks/{keyword}")
    @ResponseBody
    RankingResponse getAggregatedRanksPerAsin(@PathVariable String keyword)
    {
        List<Ranking> rankings = rankingsService.calculateAggregatedRanksPerAsin(keyword);
        return new RankingResponse(rankings);
    }

    /**
     * Retrieves a time series containing the aggregated ranks of all ASINs for a certain keyword.
     *
     * @param keyword the key word to look for.
     *
     * @return a {@link RankingResponse} containing the list of rankings.
     */
    @GetMapping("/keyword-ranks/{keyword}")
    @ResponseBody
    RankingResponse getAggregatedRanksForAllAsins(@PathVariable String keyword)
    {
        List<Ranking> rankings = rankingsService.calculateAggregatedRanksForAllAsins(keyword);
        return new RankingResponse(rankings);
    }

    /**
     * Retrieves a time series containing the aggregated ranks per keywords for a certain ASIN.
     *
     * @param asin the asin to look for.
     *
     * @return a {@link RankingResponse} containing the list of rankings.
     */
    @GetMapping("/detailed-asin-ranks/{asin}")
    @ResponseBody
    RankingResponse getAggregatedRanksPerKeyword(@PathVariable String asin)
    {
        List<Ranking> rankings = rankingsService.calculateAggregatedRanksPerKeyword(asin);
        return new RankingResponse(rankings);
    }

    /**
     * Retrieves a time series containing the aggregated ranks for all keywords for a certain ASIN.
     *
     * @param asin the asin to look for.
     *
     * @return a {@link RankingResponse} containing the list of rankings.
     */
    @GetMapping("/asin-ranks/{asin}")
    @ResponseBody
    RankingResponse getAggregatedRanksForAllKeywords(@PathVariable String asin)
    {
        List<Ranking> rankings = rankingsService.calculateAggregatedRanksForAllKeywords(asin);
        return new RankingResponse(rankings);
    }
}

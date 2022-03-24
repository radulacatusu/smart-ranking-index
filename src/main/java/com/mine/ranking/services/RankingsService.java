package com.mine.ranking.services;

import com.mine.ranking.Initializers.RankingsInitializer;
import com.mine.ranking.dtos.Ranking;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

/**
 * Provides the necessary methods to calculate the rankings.
 */
@Service
@RequiredArgsConstructor
public class RankingsService
{

    private final RankingsInitializer rankingsInitializer;

    /**
     * Retrieves a list of individual rankings for the provided keyword.
     *
     * @param keyword the key word to look for.
     *
     * @return a list of {@link Ranking}s.
     */
    public List<Ranking> calculateIndividualRankings(final String keyword)
    {
        return getAllRankings()
            .stream()
            // Filters the rankings by keyword.
            .filter(r -> r.getKeyword().equals(keyword))
            .sorted(Comparator.comparing(Ranking::getAsin))
            .sorted(Comparator.comparing(Ranking::getTimestamp))
            .toList();
    }

    /**
     * Retrieves a list of individual rankings for the provided keyword and asin.
     *
     * @param keyword the keyword to look for.
     * @param asin the asin to look for.
     *
     * @return a list of {@link Ranking}s.
     */
    public List<Ranking> calculateIndividualRankings(final String keyword, final String asin)
    {
        return getAllRankings()
            .stream()
            // Filters the rankings by keyword.
            .filter(r -> r.getKeyword().equals(keyword))
            // Filters the rankings by asin.
            .filter(r -> r.getAsin().equals(asin))
            .sorted(Comparator.comparing(Ranking::getTimestamp))
            .toList();
    }

    /**
     * Retrieves a list of aggregated ranks for all ASINs for the provided keyword.
     *
     * @param keyword the key word to look for.
     *
     * @return a list of {@link Ranking}s.
     */
    public List<Ranking> calculateAggregatedRanksPerAsin(final String keyword)
    {

        return getAllRankings()
            .stream()
            // Filters the rankings by keyword.
            .filter(r -> r.getKeyword().equals(keyword))
            // Groups the rankings by timestamp.
            .collect(Collectors.groupingBy(Ranking::getTimestamp))
            .entrySet()
            .stream()
            // Collects the rankings in a tree map having as key the timestamp and as value the Ranking.
            .collect(Collectors.toMap(
                Map.Entry::getKey,
                rank -> rank.getValue()
                            .stream()
                            // Groups the rankings by asin.
                            .collect(Collectors.groupingBy(Ranking::getAsin))
                            .entrySet().stream()
                            .map(newRank -> Ranking
                                .builder()
                                .timestamp(rank.getKey())
                                .asin(newRank.getKey())
                                .rank(newRank.getValue()
                                             .stream()
                                             // Aggregates the rankings.
                                             .reduce(0, (partialRank, ranking) -> partialRank + ranking.getRank(), Integer::sum))
                                .build()).toList(),
                (s, s2) -> s, TreeMap::new))
            .values().stream().toList()
            // Returns the rankings as a list.
            .stream().flatMap(Collection::stream).toList();
    }

    /**
     * Retrieves a list of aggregated ranks of all ASINs for the provided keyword.
     *
     * @param keyword the key word to look for.
     *
     * @return a list of {@link Ranking}s.
     */
    public List<Ranking> calculateAggregatedRanksForAllAsins(final String keyword)
    {
        return getAllRankings()
            .stream()
            // Filters the rankings by keyword.
            .filter(r -> r.getKeyword().equals(keyword))
            // Groups the rankings by timestamp.
            .collect(Collectors.groupingBy(Ranking::getTimestamp))
            .entrySet()
            .stream()
            // Collects the rankings in a tree map having as key the timestamp and as value the rank value.
            .collect(Collectors.toMap(
                Map.Entry::getKey,
                rank -> rank.getValue()
                            .stream()
                            // Aggregates the rankings.
                            .reduce(0, (partialRank, ranking) -> partialRank + ranking.getRank(), Integer::sum),
                (s, s2) -> s, TreeMap::new))
            .entrySet()
            .stream().map(z -> Ranking
                .builder()
                .timestamp(z.getKey())
                .rank(z.getValue())
                .build())
            .toList();
    }

    /**
     * Retrieves a list of aggregated ranks per keyword for the provided asin.
     *
     * @param asin the asin to look for.
     *
     * @return a list of {@link Ranking}s.
     */
    public List<Ranking> calculateAggregatedRanksPerKeyword(final String asin)
    {
        return getAllRankings()
            .stream()
            // Filters the rankings by ASIN.
            .filter(r -> r.getAsin().equals(asin))
            // Groups the rankings by timestamp.
            .collect(Collectors.groupingBy(Ranking::getTimestamp))
            .entrySet()
            .stream()
            // Collects the rankings in a tree map having as key the timestamp and as value the Ranking.
            .collect(Collectors.toMap(
                Map.Entry::getKey,
                rank -> rank.getValue()
                            .stream()
                            // Groups the rankings by keyword.
                            .collect(Collectors.groupingBy(Ranking::getKeyword))
                            .entrySet().stream()
                            .map(newRank -> Ranking
                                .builder()
                                .timestamp(rank.getKey())
                                .keyword(newRank.getKey())
                                .rank(newRank.getValue()
                                             .stream()
                                             // Aggregates the rankings.
                                             .reduce(0, (partialRank, ranking) -> partialRank + ranking.getRank(), Integer::sum))
                                .build()).toList(),
                (s, s2) -> s, TreeMap::new)).values().stream().toList()
            .stream().flatMap(Collection::stream).toList();
    }

    /**
     * Retrieves a list of aggregated ranks for all keywords for the provided asin.
     *
     * @param asin the asin to look for.
     *
     * @return a list of {@link Ranking}s.
     */
    public List<Ranking> calculateAggregatedRanksForAllKeywords(final String asin)
    {
        return getAllRankings()
            .stream()
            // Filters the rankings by ASIN.
            .filter(r -> r.getAsin().equals(asin))
            // Groups the rankings by timestamp.
            .collect(Collectors.groupingBy(Ranking::getTimestamp))
            .entrySet()
            .stream()
            // Collects the rankings in a tree map having as key the timestamp and as value the rank value.
            .collect(Collectors.toMap(
                Map.Entry::getKey,
                rank -> rank.getValue()
                            .stream()
                            // Aggregates the rankings.
                            .reduce(0, (partialRank, ranking) -> partialRank + ranking.getRank(), Integer::sum),
                // order the ranks by timestamp
                (s, s2) -> s, TreeMap::new))
            .entrySet()
            .stream().map(rank -> Ranking
                .builder()
                .timestamp(rank.getKey())
                .rank(rank.getValue())
                .build())
            .toList();
    }

    private List<Ranking> getAllRankings()
    {
        return rankingsInitializer.getRankings();
    }
}

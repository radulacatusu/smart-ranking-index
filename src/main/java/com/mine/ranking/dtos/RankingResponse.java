package com.mine.ranking.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * Represents a response containing a list of rankings.
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RankingResponse implements Serializable
{

    @Serial
    private static final long serialVersionUID = -7008319595854695238L;

    private List<Ranking> rankings;

}

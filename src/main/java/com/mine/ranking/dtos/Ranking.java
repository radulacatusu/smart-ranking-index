package com.mine.ranking.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.ZonedDateTime;

/**
 * Represents the ranking of a product.
 */
@Getter
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class Ranking implements Serializable
{

    @Serial
    private static final long serialVersionUID = -5445070368033927371L;

    /**
     * Identifies the timestamp of the product.
     */
    private ZonedDateTime timestamp;

    /**
     * Identifies the asin of the product.
     */
    private String asin;

    /**
     * Identifies the keyword of the product.
     */
    private String keyword;

    /**
     * Identifies the rank of the product.
     */
    private Integer rank;
}

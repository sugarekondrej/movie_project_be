package dev.sugarek.movies.dto;

import lombok.Data;

@Data
public class RatingCreate {
    private String imdbId;
    private Double ratingValue;
}

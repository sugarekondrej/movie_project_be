package dev.sugarek.movies.dto;

import lombok.Data;

@Data
public class ReviewCreate {
    private String reviewBody;
    private String imdbId;
}

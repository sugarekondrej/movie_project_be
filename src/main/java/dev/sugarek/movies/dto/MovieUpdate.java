package dev.sugarek.movies.dto;

import lombok.Data;

import java.util.List;

@Data
public class MovieUpdate {
    private String title;
    private String director;
    private String releaseDate;
    private String trailerLink;
    private String poster;
    private List<String> genres;
    private List<String> backdrops;
    private boolean favorite;
}

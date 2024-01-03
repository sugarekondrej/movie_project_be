package dev.sugarek.movies.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.util.List;

@Document(collection = "movies")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Movie {

    @Id
    private ObjectId id;

    private String imdbId;

    private String title;

    private  String director;

    private String releaseDate;

    private String trailerLink;

    private String poster;

    private List<String> genres;

    private List<String> backdrops;

    private boolean favorite = false;
    @DocumentReference
    private List<Review> reviewIds;

    @DocumentReference
    private List<Rating> ratingIds;

    public Movie(String imdbId, String title, String director, String releaseDate, String trailerLink, String poster, List<String> genres, List<String> backdrops, boolean favorite) {
        this.imdbId = imdbId;
        this.title = title;
        this.director = director;
        this.releaseDate = releaseDate;
        this.trailerLink = trailerLink;
        this.poster = poster;
        this.genres = genres;
        this.backdrops = backdrops;
        this.favorite = favorite;

    }
}

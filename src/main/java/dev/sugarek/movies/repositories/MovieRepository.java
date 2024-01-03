package dev.sugarek.movies.repositories;

import dev.sugarek.movies.entities.Movie;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MovieRepository extends MongoRepository<Movie, ObjectId> {
    Optional<Movie> findMovieByImdbId(String imdbId);

    List<Movie> findMovieByTitleStartingWithIgnoreCaseAndDirectorContaining(String title, String director);

    List<Movie> findMovieByTitleStartingWithIgnoreCase(String title);

    List<Movie> findMovieByDirectorContaining(String director);
    List<Movie> findMovieByFavorite(boolean favorite);
}

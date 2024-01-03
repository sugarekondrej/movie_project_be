package dev.sugarek.movies.services;

import dev.sugarek.movies.dto.MovieCreate;
import dev.sugarek.movies.dto.MovieUpdate;
import dev.sugarek.movies.entities.Movie;
import dev.sugarek.movies.repositories.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MovieService {

    @Autowired
    private MovieRepository movieRepository;

    public List<Movie> AllMovies() {
        return movieRepository.findAll();

    }

    public Optional<Movie> getMovie(String imdbId) {
        return movieRepository.findMovieByImdbId(imdbId);
    }

    public Movie createMovie(MovieCreate movieCreate) {

        if (movieCreate.getImdbId() == null || movieCreate.getImdbId().isEmpty()) {
            throw new IllegalArgumentException("IMDb ID cannot be null or empty");
        }

        if (movieCreate.getTitle() == null || movieCreate.getTitle().isEmpty()) {
            throw new IllegalArgumentException("Title cannot be null or empty");
        }

        if (movieCreate.getDirector() == null || movieCreate.getDirector().isEmpty()) {
            throw new IllegalArgumentException("Director cannot be null or empty");
        }

        if (movieCreate.getReleaseDate() == null || movieCreate.getReleaseDate().isEmpty()) {
            throw new IllegalArgumentException("Release date cannot be null or empty");
        }

        if (movieCreate.getTrailerLink() == null || movieCreate.getTrailerLink().isEmpty()) {
            throw new IllegalArgumentException("Trailer link cannot be null or empty");
        }

        if (movieCreate.getPoster() == null || movieCreate.getPoster().isEmpty()) {
            throw new IllegalArgumentException("Poster link cannot be null or empty");
        }

        if (movieCreate.getGenres() == null || movieCreate.getGenres().isEmpty()) {
            throw new IllegalArgumentException("Genres cannot be null or empty");
        }

        if (movieCreate.getBackdrops() == null || movieCreate.getBackdrops().isEmpty()) {
            throw new IllegalArgumentException("Backdrops cannot be null or empty");
        }

        Movie movie = new Movie(
                movieCreate.getImdbId(),
                movieCreate.getTitle(),
                movieCreate.getDirector(),
                movieCreate.getReleaseDate(),
                movieCreate.getTrailerLink(),
                movieCreate.getPoster(),
                movieCreate.getGenres(),
                movieCreate.getBackdrops(),
                movieCreate.isFavorite()
        );

        return movieRepository.insert(movie);
    }

    public Movie updateMovie(String imdbId, MovieUpdate movieUpdate){
        Movie existingMovie = movieRepository.findMovieByImdbId(imdbId)
                .orElseThrow(() -> new RuntimeException("Movie not found"));

        if (movieUpdate.getTitle() != null && !movieUpdate.getTitle().isEmpty()) {
            existingMovie.setTitle(movieUpdate.getTitle());
        }

        if (movieUpdate.getDirector() != null) {
            existingMovie.setDirector(movieUpdate.getDirector());
        }

        if (movieUpdate.getReleaseDate() != null) {
            existingMovie.setReleaseDate(movieUpdate.getReleaseDate());
        }

        if (movieUpdate.getTrailerLink() != null) {
            existingMovie.setTrailerLink(movieUpdate.getTrailerLink());
        }

        if (movieUpdate.getPoster() != null) {
            existingMovie.setPoster(movieUpdate.getPoster());
        }

        if (movieUpdate.getGenres() != null && !movieUpdate.getGenres().isEmpty()) {
            existingMovie.setGenres(movieUpdate.getGenres());
        }

        if (movieUpdate.getBackdrops() != null && !movieUpdate.getBackdrops().isEmpty()) {
            existingMovie.setBackdrops(movieUpdate.getBackdrops());
        }

        if (!movieUpdate.isFavorite() && existingMovie.isFavorite()) {
            existingMovie.setFavorite(false);
        } else if (movieUpdate.isFavorite() && !existingMovie.isFavorite()) {
            existingMovie.setFavorite(true);
        }

        return movieRepository.save(existingMovie);
    }

    public boolean deleteMovie(String imdbId) {

        Movie movieToDelete = movieRepository.findMovieByImdbId(imdbId)
                .orElseThrow(() -> new RuntimeException("Movie not found"));

        if (movieToDelete == null) {
            return false;
        }

        movieRepository.delete(movieToDelete);
        return true;
    }

    public Optional<Movie> searchMovie(String titleKeyword, String directorKeyword) {
        if ((titleKeyword == null || titleKeyword.isBlank()) && (directorKeyword == null || directorKeyword.isBlank())) {
            throw new IllegalArgumentException("At least one of titleKeyword or directorKeyword must be provided");
        }

        List<Movie> movies;
        if (titleKeyword != null && !titleKeyword.isBlank() && directorKeyword != null && !directorKeyword.isBlank()) {
            movies = movieRepository.findMovieByTitleStartingWithIgnoreCaseAndDirectorContaining(titleKeyword, directorKeyword);
        } else if (titleKeyword != null && !titleKeyword.isBlank()) {
            movies = movieRepository.findMovieByTitleStartingWithIgnoreCase(titleKeyword);
        } else {
            movies = movieRepository.findMovieByDirectorContaining(directorKeyword);
        }

        return movies.stream().findAny();
    }

    public List<Movie> filterByFavorite(boolean favorite) {
        return movieRepository.findMovieByFavorite(favorite);
    }

}

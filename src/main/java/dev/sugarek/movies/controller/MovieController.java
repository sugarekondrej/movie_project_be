package dev.sugarek.movies.controller;

import dev.sugarek.movies.dto.MovieCreate;
import dev.sugarek.movies.dto.MovieUpdate;
import dev.sugarek.movies.model.Movie;
import dev.sugarek.movies.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/movies")
public class MovieController {

    @Autowired
    private MovieService service;

    @GetMapping
    public ResponseEntity<List<Movie>> getMovies() {
        return new ResponseEntity<List<Movie>>(service.AllMovies(), HttpStatus.OK);
    }

    @GetMapping("/{imdbId}")
    public ResponseEntity<?> getMovie(@PathVariable String imdbId) {
        if (imdbId == null || imdbId.trim().isEmpty()){
            return new ResponseEntity<>("Imdb ID cannot be null or empty", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<Optional<Movie>>(service.getMovie(imdbId), HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<Movie> createMovie(@RequestBody MovieCreate movieCreate) {
        try {
            Movie createdMovie = service.createMovie(movieCreate);
            return new ResponseEntity<>(createdMovie, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{imdbId}")
    public ResponseEntity<?> updateMovie(@PathVariable String imdbId, @RequestBody MovieUpdate movieUpdate) {

        if (movieUpdate.getTitle() == null || movieUpdate.getTitle().isEmpty()) {
            return new ResponseEntity<>("Title must not be null or empty", HttpStatus.BAD_REQUEST);
        }

        if (movieUpdate.getDirector() == null || movieUpdate.getDirector().isEmpty()) {
            return new ResponseEntity<>("Director must not be null or empty", HttpStatus.BAD_REQUEST);
        }

        if (movieUpdate.getGenres() == null || movieUpdate.getGenres().isEmpty()) {
            return new ResponseEntity<>("Genres must not be null or empty", HttpStatus.BAD_REQUEST);
        }

        if (movieUpdate.getBackdrops() == null || movieUpdate.getBackdrops().isEmpty()) {
            return new ResponseEntity<>("Backdrops must not be null or empty", HttpStatus.BAD_REQUEST);
        }


        Movie updatedMovie = service.updateMovie(imdbId, movieUpdate);

        return new ResponseEntity<>(updatedMovie, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{imdbId}")
    public ResponseEntity<Boolean> deleteMovie(@PathVariable String imdbId) {
        boolean deleted = service.deleteMovie(imdbId);

        if (deleted) {
            return new ResponseEntity<>(true, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(false, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchMovie(@RequestParam(required = false) String titleKeyword,
                                         @RequestParam(required = false) String directorKeyword) {
        try {
            if (titleKeyword != null || directorKeyword != null) {
                Optional<Movie> result = service.searchMovie(titleKeyword, directorKeyword);

                if (result.isPresent()) {
                    return new ResponseEntity<>(result.get(), HttpStatus.OK);
                } else {
                    return new ResponseEntity<>("No movies found with the given criteria", HttpStatus.NOT_FOUND);
                }
            } else {
                return new ResponseEntity<>("At least one of titleKeyword or directorKeyword must be provided",
                        HttpStatus.BAD_REQUEST);
            }
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/filter")
    public ResponseEntity<List<Movie>> filterByFavorite(@RequestParam(required = true) boolean favorite) {
        try {
            List<Movie> filteredMovies = service.filterByFavorite(favorite);
            return new ResponseEntity<>(filteredMovies, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }




}

package dev.sugarek.movies.controller;

import dev.sugarek.movies.model.Rating;
import dev.sugarek.movies.service.RatingService;
import dev.sugarek.movies.dto.RatingCreate;
import dev.sugarek.movies.dto.RatingUpdate;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("ratings")
public class RatingController {

    @Autowired
    private RatingService ratingService;

    @GetMapping("/{imdbId}")
    public ResponseEntity<?> getAverageRating(@PathVariable String imdbId){
        if (imdbId == null || imdbId.trim().isEmpty()){
            return new ResponseEntity<>("Imdb ID cannot be null or empty", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<Double>(ratingService.averageRatingForMovie(imdbId), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> createRating(@RequestBody RatingCreate ratingCreate) {
        String imdbId = ratingCreate.getImdbId();

        if (imdbId == null || imdbId.trim().isEmpty()) {
            return new ResponseEntity<>("Imdb ID cannot be null or empty", HttpStatus.BAD_REQUEST);
        }

        Double ratingValue = ratingCreate.getRatingValue();

        if (ratingValue == null) {
            return new ResponseEntity<>("Invalid value for ratingValue.", HttpStatus.BAD_REQUEST);
        }

        Rating createdRating = ratingService.createRating(ratingValue, imdbId);

        return new ResponseEntity<>(createdRating, HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<?> updateRating(@RequestBody RatingUpdate ratingUpdate) {
        ObjectId id = ratingUpdate.getId();

        if (id == null) {
            throw new IllegalArgumentException("ID cannot be null");
        }

        String ratingValueStr = ratingUpdate.getRatingValue().toString();

        if (ratingValueStr == null || ratingValueStr.trim().isEmpty()) {
            return new ResponseEntity<>("Invalid value for ratingValue.", HttpStatus.BAD_REQUEST);
        }

        Double ratingValue;
        try {
            ratingValue = Double.valueOf(ratingValueStr);
        } catch (NumberFormatException e) {
            return new ResponseEntity<>("Invalid value for ratingValue.", HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(ratingService.updateRating(id, ratingValue), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteRating(@PathVariable ObjectId id) {
        boolean deleted = ratingService.deleteRating(id);

        if (deleted) {
            return new ResponseEntity<>("Rating deleted successfully.", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Rating not found.", HttpStatus.NOT_FOUND);
        }
    }

}

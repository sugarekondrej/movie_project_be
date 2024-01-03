package dev.sugarek.movies.controllers;

import dev.sugarek.movies.dto.ReviewCreate;
import dev.sugarek.movies.entities.Review;
import dev.sugarek.movies.services.ReviewService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("reviews")
public class ReviewController {
    @Autowired
    private ReviewService reviewService;

    @PostMapping
    public ResponseEntity<Review> createReview(@RequestBody ReviewCreate reviewCreate) {
        return new ResponseEntity<Review>(reviewService.createReview(reviewCreate.getReviewBody(), reviewCreate.getImdbId()), HttpStatus.CREATED);
    }

    public ResponseEntity<?> updateReview(ObjectId id, String reviewBody) {

        if(reviewBody == null || reviewBody.isEmpty()) {
            return new ResponseEntity<>("reviewBody must not be null or empty",HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<Review>(reviewService.updateReview(id,reviewBody), HttpStatus.OK);
    }

    public ResponseEntity<?> deleteReview(ObjectId id) {
        boolean deleted = reviewService.deleteReview(id);

        if (deleted) {
            return new ResponseEntity<>("Review deleted successfully.", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Review not found.", HttpStatus.NOT_FOUND);
        }
    }
}

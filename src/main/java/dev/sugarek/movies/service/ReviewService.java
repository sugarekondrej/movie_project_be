package dev.sugarek.movies.service;

import dev.sugarek.movies.model.Movie;
import dev.sugarek.movies.model.Review;
import dev.sugarek.movies.repository.ReviewRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;


@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    @PostMapping
    public Review createReview(String reviewBody, String imdbId) {

        Review review = reviewRepository.insert(new Review(reviewBody));

        mongoTemplate.update(Movie.class)
                .matching(Criteria.where("imdbId").is(imdbId))
                .apply(new Update().push("reviewIds").value(review))
                .first();

        return review;
    }

    public Review updateReview(ObjectId id, String reviewBody) {
        Review review = reviewRepository.findById(id).orElseThrow(() -> new RuntimeException("Review not found"));

        review.setBody(reviewBody);

        reviewRepository.save(review);

        return review;

    }

    public boolean deleteReview(ObjectId id) {
        Review review = reviewRepository.findById(id).orElse(null);

        if (review != null){
            reviewRepository.delete(review);
            return true;
        } else{
            return false;
        }

    }
}

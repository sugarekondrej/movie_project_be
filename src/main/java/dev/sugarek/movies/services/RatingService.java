package dev.sugarek.movies.services;

import dev.sugarek.movies.entities.Movie;
import dev.sugarek.movies.entities.Rating;
import dev.sugarek.movies.repositories.RatingRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RatingService {

    @Autowired
    private RatingRepository ratingRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    public Double averageRatingForMovie(String imdbId){
        Query movieQuery = new Query(Criteria.where("imdbId").is(imdbId));
        Movie movie = mongoTemplate.findOne(movieQuery,Movie.class);

        if (movie == null){
            throw new RuntimeException("Movie with imdbId " + imdbId + " not found");
        }


        List<ObjectId> ratingIds = movie.getRatingIds()
                .stream()
                .map(Rating::getId)
                .collect(Collectors.toList());

        if (ratingIds == null || ratingIds.isEmpty()){
            throw new RuntimeException("List of ratingIds is null or empty");
        }

        List<Double> ratingValues = ratingRepository.findAllById(ratingIds)
                .stream()
                .map(Rating::getRatingValue)
                .collect(Collectors.toList());

        if (ratingValues.isEmpty()){
            return 0.0;
        }
        double sum = 0.0;

        for (Double value : ratingValues){
            sum += value;
        }
        double average = (sum > 0) ? sum / ratingValues.size() : 0.0;

        return  average;

    }

    public Rating createRating(Double ratingValue, String imdbId){
        System.out.println("Received ratingValue: " + ratingValue);
        System.out.println("Received imdbId: " + imdbId);

        Rating rating = ratingRepository.insert(new Rating(ratingValue));


        Query query = new Query(Criteria.where("imdbId").is(imdbId));
        Update update = new Update().push("ratingIds", rating);

        mongoTemplate.findAndModify(query,update,Movie.class);

        System.out.println(rating);

        return rating;


    }

    public Rating updateRating(ObjectId id, Double ratingValue) {
        Optional<Rating> optionalExistingRating = ratingRepository.findById(id);

        Rating existingRating = optionalExistingRating
                .orElseThrow(() -> new RuntimeException("Rating not found"));

        existingRating.setRatingValue(ratingValue);

        ratingRepository.save(existingRating);

        return existingRating;
    }

    public boolean deleteRating(ObjectId id) {
        Rating existingRating = ratingRepository.findById(id).orElse(null);

        if (existingRating != null) {
            ratingRepository.delete(existingRating);
            return true;
        } else {
            return false;
        }
    }

}

package dev.sugarek.movies.repositories;

import dev.sugarek.movies.entities.Rating;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RatingRepository extends MongoRepository<Rating, ObjectId> {

}
package dev.sugarek.movies.dto;

import lombok.Data;
import org.bson.types.ObjectId;

@Data
public class RatingUpdate {
    private ObjectId id;
    private Double ratingValue;
}

package dev.sugarek.movies.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "movies")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Rating {

    @Id
    private ObjectId id;

    private Double ratingValue;

    public Rating(Double ratingValue) {
        this.ratingValue = ratingValue;
    }
}

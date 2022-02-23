package org.example.movieservice.repository;

import org.example.movieservice.entity.Rating;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

public interface RatingRepository extends JpaRepository<Rating, Integer> {
    Optional<List<Rating>> findRatingByMovieId(Integer movieId);
    Optional<Rating> findRatingByMovieIdAndRateBy(Integer movieId, String username);
}

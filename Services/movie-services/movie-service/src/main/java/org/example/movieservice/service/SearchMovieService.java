package org.example.movieservice.service;

import lombok.AllArgsConstructor;
import org.example.common.omdb.OmdbMovieDetails;
import org.example.common.omdb.OmdbServiceClient;
import org.example.movieservice.entity.*;
import org.example.movieservice.repository.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;

import static org.example.movieservice.entity.SearchError.*;

@Service
@AllArgsConstructor
public class SearchMovieService {
    private MovieRepository movieRepository;
    private RatingRepository ratingRepository;
    private SearchRecordRepository searchRecordRepository;
    private OmdbServiceClient omdbServiceClient;

    public ResponseEntity<?> search(Optional<String> username, Optional<String> title, Optional<String> year){
        if(!username.isPresent()){
            return new ResponseEntity<SearchErrorResponse>(new SearchErrorResponse("False", USERNAME_INVALID), HttpStatus.BAD_REQUEST);
        }

        if(!title.isPresent()){
            return new ResponseEntity<SearchErrorResponse>(new SearchErrorResponse("False", TITLE_INVALID), HttpStatus.BAD_REQUEST);
        }

        // chk search record exist -
        Optional<SearchRecord> searchRecord = searchRecordRepository.findSearchRecordByTitleAndYear(title.get(), year.orElse(""));
        if (searchRecord.isPresent()){
            // chk search record exist - true -> return movie
            return returnSearchMovieResponse(username.get(), searchRecord.get().getMovie());
        } else {
            // chk search record exist - false ->
            // get movie from omdb,
            OmdbMovieDetails omdbMovieDetails = !year.isPresent() ? omdbServiceClient.getByTitle(title.get()) : omdbServiceClient.getByTitleAndYear(title.get(), year.get());
            // if  record found from -> save to db -> return movie
            if (omdbMovieDetails.getResponse().equals("True")) {
                searchRecord = Optional.of(SearchRecord.builder()
                        .movie(Movie.builder()
                                .plot(omdbMovieDetails.getPlot())
                                .duration(omdbMovieDetails.getRuntime())
                                .poster(omdbMovieDetails.getPoster())
                                .rating(omdbMovieDetails.getImdbRating())
                                .releaseYear(omdbMovieDetails.getReleased())
                                .title(omdbMovieDetails.getTitle())
                                .year(omdbMovieDetails.getYear())
                                .build())
                        .title(title.get())
                        .year(year.orElse(""))
                        .build());

                searchRecordRepository.saveAndFlush(searchRecord.get());

                return returnSearchMovieResponse(username.get(), searchRecord.get().getMovie());
            }
            // if not record found from omdb don't save to db
        }

        return new ResponseEntity<SearchErrorResponse>(new SearchErrorResponse("False", MOVIE_NOT_FOUND), HttpStatus.OK);
    }

    public ResponseEntity<SearchMovieResponse> returnSearchMovieResponse(String username, Movie movie){
        List<Rating> ratings = getRatingByMovieId(movie.getId());

        // chk how many likes or dislikes
        int countLike = 0, countDislike = 0;
        for(Rating rating : ratings){
            if(rating.isRateType()) countLike++;
            else countDislike++;
        }
        // chk is username liked or disliked
        BiFunction<List<Rating>, Boolean, Boolean> biGetLikedOrDislikeDByYou = (list, isChkForLiked) -> {
            if(isChkForLiked && list.stream().filter(v -> v.getRateBy().equals(username)).anyMatch(v -> v.isRateType())) {
                return true;
            } else if(!isChkForLiked && list.stream().filter(v -> v.getRateBy().equals(username)).anyMatch(v -> !v.isRateType())) {
                return true;
            }
            return false;
        };

        return new ResponseEntity<SearchMovieResponse>(
                SearchMovieResponse.builder()
                .movieId(movie.getId())
                .title(movie.getTitle())
                .year(movie.getYear())
                .releaseYear(movie.getReleaseYear())
                .duration(movie.getDuration())
                .rating(movie.getRating())
                .poster(movie.getPoster())
                .totalLike(countLike)
                .totalDislike(countDislike)
                .response("True")
                .isLikedByYou(biGetLikedOrDislikeDByYou.apply(ratings, true))
                .isDislikedByYou(biGetLikedOrDislikeDByYou.apply(ratings, false))
                .build(),
                HttpStatus.OK);
    }

    public List<Rating> getRatingByMovieId(Integer movieId){
        return ratingRepository.findRatingByMovieId(movieId).orElse(new ArrayList<>());
    }

    public ResponseEntity rating(Optional<Rating> rating){
        if(!rating.isPresent()){
            return new ResponseEntity<SearchErrorResponse>(new SearchErrorResponse("False", RATING_NOT_FOUND), HttpStatus.BAD_REQUEST);
        }

        //chk user rate status
        Optional<Rating> userRating = ratingRepository.findRatingByMovieIdAndRateBy(rating.get().getMovieId(), rating.get().getRateBy());

        if(userRating.isPresent()){
            ratingRepository.deleteById(userRating.get().getId());
            //if rating status is the same -> undo rating
           //else rating

        }

        if(!userRating.isPresent() || !(rating.get().isRateType() == userRating.get().isRateType()) || !(!rating.get().isRateType() == !userRating.get().isRateType())){
            ratingRepository.save(rating.get());
        }


        return new ResponseEntity(HttpStatus.OK);
    }

    public ResponseEntity delete(Optional<Integer> movieId){
        if(!movieId.isPresent()){
            return new ResponseEntity<SearchErrorResponse>(new SearchErrorResponse("False", MOVIE_NOT_FOUND), HttpStatus.BAD_REQUEST);
        }
        searchRecordRepository.deleteSearchRecordByMovieId(movieId.get());
        return new ResponseEntity(HttpStatus.OK);
    }

    public ResponseEntity update(Optional<SearchMovieResponse> searchMovieResponse){
        if(!searchMovieResponse.isPresent()){
            return new ResponseEntity<SearchErrorResponse>(new SearchErrorResponse("False", MOVIE_NOT_FOUND), HttpStatus.BAD_REQUEST);
        }

        Movie movie = movieRepository.findById(searchMovieResponse.get().getMovieId()).orElse(null);
        if(movie!=null) {
            movie.setTitle(searchMovieResponse.get().getTitle());
            movie.setDuration(searchMovieResponse.get().getDuration());
            movie.setPoster(searchMovieResponse.get().getPoster());
            movie.setRating(searchMovieResponse.get().getRating());
            movie.setYear(searchMovieResponse.get().getYear());
            movie.setReleaseYear(searchMovieResponse.get().getReleaseYear());
            movie.setPlot(searchMovieResponse.get().getDesc());
            movieRepository.save(movie);
        }

        return new ResponseEntity(HttpStatus.OK);
    }
}

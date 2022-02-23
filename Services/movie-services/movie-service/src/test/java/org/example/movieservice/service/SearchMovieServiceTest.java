package org.example.movieservice.service;

import com.netflix.discovery.converters.Auto;
import org.example.movieservice.entity.Rating;
import org.example.movieservice.entity.SearchError;
import org.example.movieservice.entity.SearchErrorResponse;
import org.example.movieservice.entity.SearchMovieResponse;
import org.example.movieservice.repository.RatingRepository;
import org.example.movieservice.repository.SearchRecordRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.example.movieservice.entity.SearchError.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class SearchMovieServiceTest {

    @Autowired
    private RatingRepository ratingRepository;
    @Autowired
    private SearchRecordRepository searchRecordRepository;
    @Autowired
    private SearchMovieService searchMovieService;

    @BeforeEach
    void setup(){
        ratingRepository.deleteAll();
        searchRecordRepository.deleteAll();
    }

    //error case
    @Test
    void shouldReturnErrorIfUsernameNotPresented(){
        ResponseEntity<SearchErrorResponse> res =
                (ResponseEntity<SearchErrorResponse>)searchMovieService.search(Optional.ofNullable(null), Optional.of("Harry"), Optional.ofNullable(null));
        assertTrue(res.getStatusCode().is4xxClientError());
        assertTrue(res.getBody().error().equals(USERNAME_INVALID));
    }

    @Test
    void shouldReturnErrorIfTitleNotPresented(){
        ResponseEntity<SearchErrorResponse> res =
                (ResponseEntity<SearchErrorResponse>)searchMovieService.search( Optional.of("Harry"), Optional.ofNullable(null), Optional.ofNullable(null));
        assertTrue(res.getStatusCode().is4xxClientError());
        assertTrue(res.getBody().error().equals(TITLE_INVALID));
    }

    //search case
    @Test
    void shouldReturnOKIfSearchByTitle(){
        ResponseEntity<SearchMovieResponse> res =
                (ResponseEntity<SearchMovieResponse>)searchMovieService.search( Optional.of("ABC123"), Optional.ofNullable("harry"), Optional.ofNullable(null));
        assertTrue(res.getStatusCode().is2xxSuccessful());
        assertTrue(res.getBody().getTitle().equals("Harry Potter and the Deathly Hallows: Part 2"));
    }

    @Test
    void shouldReturnOKIfSearchByTitleAndYear(){
        ResponseEntity<SearchMovieResponse> res =
                (ResponseEntity<SearchMovieResponse>)searchMovieService.search( Optional.of("ABC123"), Optional.ofNullable("aaa"), Optional.ofNullable("1992"));
        assertTrue(res.getStatusCode().is2xxSuccessful());
        assertTrue(res.getBody().getTitle().equals("AAA"));

         res = (ResponseEntity<SearchMovieResponse>)searchMovieService.search( Optional.of("ABC123"), Optional.ofNullable("aaa"), Optional.ofNullable("1992"));
        assertTrue(res.getStatusCode().is2xxSuccessful());
        assertTrue(res.getBody().getTitle().equals("AAA"));
    }

    //if no result from omdb
    @Test
    void shouldReturnMovieNotFound(){
        ResponseEntity<SearchErrorResponse> res =
                (ResponseEntity<SearchErrorResponse>)searchMovieService.search( Optional.of("ABC123"), Optional.ofNullable("dsasfghtryyhjd"), Optional.ofNullable(null));
        assertTrue(res.getStatusCode().is2xxSuccessful());
        assertTrue(res.getBody().error().equals(MOVIE_NOT_FOUND));
    }

    //--------------------rating
    //error case
    @Test
    void shouldReturnErrorIfRatingIsNotPresented(){
        ResponseEntity<SearchMovieResponse> res =
                (ResponseEntity<SearchMovieResponse>)searchMovieService.search( Optional.of("ABC123"), Optional.ofNullable("harry"), Optional.ofNullable(null));
        assertTrue(res.getStatusCode().is2xxSuccessful());
        assertTrue(res.getBody().getTitle().equals("Harry Potter and the Deathly Hallows: Part 2"));

        ResponseEntity<SearchErrorResponse> resRating = (ResponseEntity<SearchErrorResponse>)searchMovieService.rating(Optional.ofNullable(null));
        assertTrue(resRating.getStatusCode().is4xxClientError());
        assertTrue(resRating.getBody().error().equals(RATING_NOT_FOUND));
    }

    @Test
    void oneUserLikeMovie(){
        String user01 = "ABC123";
        String movie01 = "harry";
        ResponseEntity<SearchMovieResponse> res =
                (ResponseEntity<SearchMovieResponse>)searchMovieService.search(Optional.of(user01), Optional.ofNullable(movie01), Optional.ofNullable(null));
        assertTrue(res.getStatusCode().is2xxSuccessful());
        assertTrue(res.getBody().getTitle().equals("Harry Potter and the Deathly Hallows: Part 2"));
        assertTrue(res.getBody().isLikedByYou() == false);
        assertTrue(res.getBody().isDislikedByYou() == false);
        assertTrue(res.getBody().getTotalLike() == 0);
        assertTrue(res.getBody().getTotalDislike() == 0);

        searchMovieService.rating(Optional.ofNullable(
                Rating.builder()
                        .movieId(res.getBody().getMovieId())
                        .rateBy(user01)
                        .rateType(true)
                        .build()));

        res = (ResponseEntity<SearchMovieResponse>)searchMovieService.search(Optional.of(user01), Optional.ofNullable(movie01), Optional.ofNullable(null));
        assertTrue(res.getStatusCode().is2xxSuccessful());
        assertTrue(res.getBody().getTitle().equals("Harry Potter and the Deathly Hallows: Part 2"));
        assertTrue(res.getBody().isLikedByYou() == true);
        assertTrue(res.getBody().isDislikedByYou() == false);
        assertTrue(res.getBody().getTotalLike() == 1);
        assertTrue(res.getBody().getTotalDislike() == 0);

        assertTrue(ratingRepository.findAll().size()==1);

    }

    @Test
    void oneUserDislikeMovie(){
        String user01 = "ABC123";
        String movie01 = "harry";
        ResponseEntity<SearchMovieResponse> res =
                (ResponseEntity<SearchMovieResponse>)searchMovieService.search(Optional.of(user01), Optional.ofNullable(movie01), Optional.ofNullable(null));
        assertTrue(res.getStatusCode().is2xxSuccessful());
        assertTrue(res.getBody().getTitle().equals("Harry Potter and the Deathly Hallows: Part 2"));
        assertTrue(res.getBody().isLikedByYou() == false);
        assertTrue(res.getBody().isDislikedByYou() == false);
        assertTrue(res.getBody().getTotalLike() == 0);
        assertTrue(res.getBody().getTotalDislike() == 0);

        searchMovieService.rating(Optional.ofNullable(
                Rating.builder()
                        .movieId(res.getBody().getMovieId())
                        .rateBy(user01)
                        .rateType(false)
                        .build()));

        res = (ResponseEntity<SearchMovieResponse>)searchMovieService.search(Optional.of(user01), Optional.ofNullable(movie01), Optional.ofNullable(null));
        assertTrue(res.getStatusCode().is2xxSuccessful());
        assertTrue(res.getBody().getTitle().equals("Harry Potter and the Deathly Hallows: Part 2"));
        assertTrue(res.getBody().isLikedByYou() == false);
        assertTrue(res.getBody().isDislikedByYou() == true);
        assertTrue(res.getBody().getTotalLike() == 0);
        assertTrue(res.getBody().getTotalDislike() == 1);

        assertTrue(ratingRepository.findAll().size()==1);
    }

    @Test
    void twoUserLikeMovieDifferentMovie(){
        String user01 = "ABC123456";
        String user02 = "ABC123";
        String movie01 = "harry";
        String movie02 = "aaa";

        ResponseEntity<SearchMovieResponse> resMovie01 =
                (ResponseEntity<SearchMovieResponse>)searchMovieService.search(Optional.of(user01), Optional.ofNullable(movie01), Optional.ofNullable(null));
        assertTrue(resMovie01.getStatusCode().is2xxSuccessful());
        assertTrue(resMovie01.getBody().getTitle().equals("Harry Potter and the Deathly Hallows: Part 2"));
        assertTrue(resMovie01.getBody().isLikedByYou() == false);
        assertTrue(resMovie01.getBody().isDislikedByYou() == false);
        assertTrue(resMovie01.getBody().getTotalLike() == 0);
        assertTrue(resMovie01.getBody().getTotalDislike() == 0);

        ResponseEntity<SearchMovieResponse> resMovie02 =
                (ResponseEntity<SearchMovieResponse>)searchMovieService.search(Optional.of(user02), Optional.ofNullable(movie02), Optional.ofNullable(null));
        assertTrue(resMovie02.getStatusCode().is2xxSuccessful());
        assertTrue(resMovie02.getBody().getTitle().equals("AAA"));
        assertTrue(resMovie02.getBody().isLikedByYou() == false);
        assertTrue(resMovie02.getBody().isDislikedByYou() == false);
        assertTrue(resMovie02.getBody().getTotalLike() == 0);
        assertTrue(resMovie02.getBody().getTotalDislike() == 0);

        searchMovieService.rating(Optional.ofNullable(
                Rating.builder()
                        .movieId(resMovie01.getBody().getMovieId())
                        .rateBy(user01)
                        .rateType(true)
                        .build()));

        searchMovieService.rating(Optional.ofNullable(
                Rating.builder()
                        .movieId(resMovie02.getBody().getMovieId())
                        .rateBy(user02)
                        .rateType(true)
                        .build()));

        resMovie01 =
                (ResponseEntity<SearchMovieResponse>)searchMovieService.search(Optional.of(user01), Optional.ofNullable(movie01), Optional.ofNullable(null));
        assertTrue(resMovie01.getStatusCode().is2xxSuccessful());
        assertTrue(resMovie01.getBody().getTitle().equals("Harry Potter and the Deathly Hallows: Part 2"));
        assertTrue(resMovie01.getBody().isLikedByYou() == true);
        assertTrue(resMovie01.getBody().isDislikedByYou() == false);
        assertTrue(resMovie01.getBody().getTotalLike() == 1);
        assertTrue(resMovie01.getBody().getTotalDislike() == 0);

        resMovie02 =
                (ResponseEntity<SearchMovieResponse>)searchMovieService.search(Optional.of(user02), Optional.ofNullable(movie02), Optional.ofNullable(null));
        assertTrue(resMovie02.getStatusCode().is2xxSuccessful());
        assertTrue(resMovie02.getBody().getTitle().equals("AAA"));
        assertTrue(resMovie02.getBody().isLikedByYou() == true);
        assertTrue(resMovie02.getBody().isDislikedByYou() == false);
        assertTrue(resMovie02.getBody().getTotalLike() == 1);
        assertTrue(resMovie02.getBody().getTotalDislike() == 0);

        assertTrue(ratingRepository.findAll().size()==2);

    }

    @Test
    void twoUserDislikeMovieDifferentMovie(){
        String user01 = "ABC123456";
        String user02 = "ABC123";
        String movie01 = "harry";
        String movie02 = "aaa";

        ResponseEntity<SearchMovieResponse> resMovie01 =
                (ResponseEntity<SearchMovieResponse>)searchMovieService.search(Optional.of(user01), Optional.ofNullable(movie01), Optional.ofNullable(null));
        assertTrue(resMovie01.getStatusCode().is2xxSuccessful());
        assertTrue(resMovie01.getBody().getTitle().equals("Harry Potter and the Deathly Hallows: Part 2"));
        assertTrue(resMovie01.getBody().isLikedByYou() == false);
        assertTrue(resMovie01.getBody().isDislikedByYou() == false);
        assertTrue(resMovie01.getBody().getTotalLike() == 0);
        assertTrue(resMovie01.getBody().getTotalDislike() == 0);

        ResponseEntity<SearchMovieResponse> resMovie02 =
                (ResponseEntity<SearchMovieResponse>)searchMovieService.search(Optional.of(user02), Optional.ofNullable(movie02), Optional.ofNullable(null));
        assertTrue(resMovie02.getStatusCode().is2xxSuccessful());
        assertTrue(resMovie02.getBody().getTitle().equals("AAA"));
        assertTrue(resMovie02.getBody().isLikedByYou() == false);
        assertTrue(resMovie02.getBody().isDislikedByYou() == false);
        assertTrue(resMovie02.getBody().getTotalLike() == 0);
        assertTrue(resMovie02.getBody().getTotalDislike() == 0);

        searchMovieService.rating(Optional.ofNullable(
                Rating.builder()
                        .movieId(resMovie01.getBody().getMovieId())
                        .rateBy(user01)
                        .rateType(false)
                        .build()));

        searchMovieService.rating(Optional.ofNullable(
                Rating.builder()
                        .movieId(resMovie02.getBody().getMovieId())
                        .rateBy(user02)
                        .rateType(false)
                        .build()));

        resMovie01 =
                (ResponseEntity<SearchMovieResponse>)searchMovieService.search(Optional.of(user01), Optional.ofNullable(movie01), Optional.ofNullable(null));
        assertTrue(resMovie01.getStatusCode().is2xxSuccessful());
        assertTrue(resMovie01.getBody().getTitle().equals("Harry Potter and the Deathly Hallows: Part 2"));
        assertTrue(resMovie01.getBody().isLikedByYou() == false);
        assertTrue(resMovie01.getBody().isDislikedByYou() == true);
        assertTrue(resMovie01.getBody().getTotalLike() == 0);
        assertTrue(resMovie01.getBody().getTotalDislike() == 1);

        resMovie02 =
                (ResponseEntity<SearchMovieResponse>)searchMovieService.search(Optional.of(user02), Optional.ofNullable(movie02), Optional.ofNullable(null));
        assertTrue(resMovie02.getStatusCode().is2xxSuccessful());
        assertTrue(resMovie02.getBody().getTitle().equals("AAA"));
        assertTrue(resMovie02.getBody().isLikedByYou() == false);
        assertTrue(resMovie02.getBody().isDislikedByYou() == true);
        assertTrue(resMovie02.getBody().getTotalLike() == 0);
        assertTrue(resMovie02.getBody().getTotalDislike() == 1);

        assertTrue(ratingRepository.findAll().size()==2);

    }

    @Test
    void twoUserLikeSameMovie(){
        String user01 = "ABC123456";
        String user02 = "ABC123";
        String movie01 = "harry";
        String movie02 = "aaa";

        ResponseEntity<SearchMovieResponse> resMovie01 =
                (ResponseEntity<SearchMovieResponse>)searchMovieService.search(Optional.of(user01), Optional.ofNullable(movie01), Optional.ofNullable(null));
        assertTrue(resMovie01.getStatusCode().is2xxSuccessful());
        assertTrue(resMovie01.getBody().getTitle().equals("Harry Potter and the Deathly Hallows: Part 2"));
        assertTrue(resMovie01.getBody().isLikedByYou() == false);
        assertTrue(resMovie01.getBody().isDislikedByYou() == false);
        assertTrue(resMovie01.getBody().getTotalLike() == 0);
        assertTrue(resMovie01.getBody().getTotalDislike() == 0);

        ResponseEntity<SearchMovieResponse> resMovie02 =
                (ResponseEntity<SearchMovieResponse>)searchMovieService.search(Optional.of(user02), Optional.ofNullable(movie02), Optional.ofNullable(null));
        assertTrue(resMovie02.getStatusCode().is2xxSuccessful());
        assertTrue(resMovie02.getBody().getTitle().equals("AAA"));
        assertTrue(resMovie02.getBody().isLikedByYou() == false);
        assertTrue(resMovie02.getBody().isDislikedByYou() == false);
        assertTrue(resMovie02.getBody().getTotalLike() == 0);
        assertTrue(resMovie02.getBody().getTotalDislike() == 0);

        searchMovieService.rating(Optional.ofNullable(
                Rating.builder()
                        .movieId(resMovie01.getBody().getMovieId())
                        .rateBy(user01)
                        .rateType(true)
                        .build()));

        searchMovieService.rating(Optional.ofNullable(
                Rating.builder()
                        .movieId(resMovie01.getBody().getMovieId())
                        .rateBy(user02)
                        .rateType(true)
                        .build()));

        resMovie01 =
                (ResponseEntity<SearchMovieResponse>)searchMovieService.search(Optional.of(user01), Optional.ofNullable(movie01), Optional.ofNullable(null));
        assertTrue(resMovie01.getStatusCode().is2xxSuccessful());
        assertTrue(resMovie01.getBody().getTitle().equals("Harry Potter and the Deathly Hallows: Part 2"));
        assertTrue(resMovie01.getBody().isLikedByYou() == true);
        assertTrue(resMovie01.getBody().isDislikedByYou() == false);
        assertTrue(resMovie01.getBody().getTotalLike() == 2);
        assertTrue(resMovie01.getBody().getTotalDislike() == 0);

        resMovie02 =
                (ResponseEntity<SearchMovieResponse>)searchMovieService.search(Optional.of(user02), Optional.ofNullable(movie01), Optional.ofNullable(null));
        assertTrue(resMovie02.getStatusCode().is2xxSuccessful());
        assertTrue(resMovie02.getBody().getTitle().equals("Harry Potter and the Deathly Hallows: Part 2"));
        assertTrue(resMovie02.getBody().isLikedByYou() == true);
        assertTrue(resMovie02.getBody().isDislikedByYou() == false);
        assertTrue(resMovie02.getBody().getTotalLike() == 2);
        assertTrue(resMovie02.getBody().getTotalDislike() == 0);

        resMovie01 =
                (ResponseEntity<SearchMovieResponse>)searchMovieService.search(Optional.of(user01), Optional.ofNullable(movie02), Optional.ofNullable(null));
        assertTrue(resMovie01.getStatusCode().is2xxSuccessful());
        assertTrue(resMovie01.getBody().getTitle().equals("AAA"));
        assertTrue(resMovie01.getBody().isLikedByYou() == false);
        assertTrue(resMovie01.getBody().isDislikedByYou() == false);
        assertTrue(resMovie01.getBody().getTotalLike() == 0);
        assertTrue(resMovie01.getBody().getTotalDislike() == 0);

        resMovie02 =
                (ResponseEntity<SearchMovieResponse>)searchMovieService.search(Optional.of(user02), Optional.ofNullable(movie02), Optional.ofNullable(null));
        assertTrue(resMovie02.getStatusCode().is2xxSuccessful());
        assertTrue(resMovie02.getBody().getTitle().equals("AAA"));
        assertTrue(resMovie02.getBody().isLikedByYou() == false);
        assertTrue(resMovie02.getBody().isDislikedByYou() == false);
        assertTrue(resMovie02.getBody().getTotalLike() == 0);
        assertTrue(resMovie02.getBody().getTotalDislike() == 0);

        assertTrue(ratingRepository.findAll().size()==2);
    }


    //-------------delete
    @Test
    void delete(){
        ResponseEntity<SearchMovieResponse> res =
                (ResponseEntity<SearchMovieResponse>)searchMovieService.search( Optional.of("ABC123"), Optional.ofNullable("harry"), Optional.ofNullable(null));
        assertTrue(res.getStatusCode().is2xxSuccessful());
        assertTrue(res.getBody().getTitle().equals("Harry Potter and the Deathly Hallows: Part 2"));

        ResponseEntity<SearchMovieResponse> res2 = (ResponseEntity<SearchMovieResponse>)searchMovieService.search( Optional.of("ABC123"), Optional.ofNullable("aaa"), Optional.ofNullable(null));
        assertTrue(res2.getStatusCode().is2xxSuccessful());
        assertTrue(res2.getBody().getTitle().equals("AAA"));

        //same record should not save to database
        res = (ResponseEntity<SearchMovieResponse>)searchMovieService.search( Optional.of("ABC123"), Optional.ofNullable("harry"), Optional.ofNullable(null));
        assertTrue(res.getStatusCode().is2xxSuccessful());
        assertTrue(res.getBody().getTitle().equals("Harry Potter and the Deathly Hallows: Part 2"));

        assertTrue(searchRecordRepository.findAll().size()==2);
        searchMovieService.delete(Optional.ofNullable(res2.getBody().getMovieId()));
        assertTrue(searchRecordRepository.findAll().size()==1);
    }

    //---------------update movie
    @Test
    void update(){
        ResponseEntity<SearchMovieResponse> res =
                (ResponseEntity<SearchMovieResponse>)searchMovieService.search( Optional.of("ABC123"), Optional.ofNullable("harry"), Optional.ofNullable(null));
        assertTrue(res.getStatusCode().is2xxSuccessful());
        assertTrue(res.getBody().getTitle().equals("Harry Potter and the Deathly Hallows: Part 2"));

        SearchMovieResponse searchMovieResponse = res.getBody();
        searchMovieResponse.setTitle("sdhfdytrseg");
        searchMovieService.update(Optional.ofNullable(searchMovieResponse));

        res = (ResponseEntity<SearchMovieResponse>)searchMovieService.search( Optional.of("ABC123"), Optional.ofNullable("harry"), Optional.ofNullable(null));
        assertTrue(res.getStatusCode().is2xxSuccessful());
        assertTrue(res.getBody().getTitle().equals("sdhfdytrseg"));
    }
}
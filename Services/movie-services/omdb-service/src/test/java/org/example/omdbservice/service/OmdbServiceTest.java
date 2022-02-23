package org.example.omdbservice.service;

import org.example.common.omdb.OmdbMovieDetails;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class OmdbServiceTest {

    @Autowired
    private OmdbService omdbService;

    @Test
    void canGetMovieDetailsFromOmdbApiByTitle(){
        OmdbMovieDetails omdbMovieDetails = omdbService.getMovieDetailsByTitle("harry");
        assertTrue(omdbMovieDetails.getTitle().equals("Harry Potter and the Deathly Hallows: Part 2"));
    }

    @Test
    void canGetMovieDetailsFromOmdbApiByTitleAndYear(){
        OmdbMovieDetails omdbMovieDetails = omdbService.getMovieDetailsByTitleAndYear("harry", "2011");
        assertTrue(omdbMovieDetails.getTitle().equals("Harry Potter and the Deathly Hallows: Part 2"));
    }

    @Test
    void shouldGetNothingIfMovieNotFound(){
        OmdbMovieDetails omdbMovieDetails = omdbService.getMovieDetailsByTitle("harrymbhgfcvbhjgv");
        assertTrue(omdbMovieDetails.getResponse().equals("False"));
    }
}
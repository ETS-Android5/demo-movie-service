package org.example.movieservice.controller;

import lombok.AllArgsConstructor;
import org.example.movieservice.entity.Movie;
import org.example.movieservice.entity.Rating;
import org.example.movieservice.entity.SearchMovieResponse;
import org.example.movieservice.service.SearchMovieService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("api/v1/movie")
@AllArgsConstructor
public class SearchMovieController {

    private SearchMovieService searchMovieService;

    @GetMapping
    public ResponseEntity search(@RequestParam(required = false) String username, @RequestParam(required = false) String title, @RequestParam(required = false) String year){
        return searchMovieService.search(Optional.ofNullable(username), Optional.ofNullable(title), Optional.ofNullable(year));
    }

    @PostMapping
    public ResponseEntity rating(@RequestBody Rating rating){
        return searchMovieService.rating(Optional.ofNullable(rating));
    }

    @DeleteMapping
    public ResponseEntity remove(@RequestBody Integer movieId){
        return searchMovieService.delete(Optional.ofNullable(movieId));
    }

    @PutMapping
    public ResponseEntity update(@RequestBody SearchMovieResponse searchMovieResponse){
        return searchMovieService.update(Optional.ofNullable(searchMovieResponse));
    }
}

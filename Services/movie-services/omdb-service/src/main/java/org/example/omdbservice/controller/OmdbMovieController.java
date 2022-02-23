package org.example.omdbservice.controller;

import lombok.AllArgsConstructor;
import org.example.common.omdb.OmdbMovieDetails;
import org.example.omdbservice.service.OmdbService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/omdb-service")
@AllArgsConstructor
public class OmdbMovieController {

    private final OmdbService omdbService;

    @GetMapping(path = "{title}")
    public OmdbMovieDetails getByTitle(@PathVariable String title){
        return omdbService.getMovieDetailsByTitle(title);
    }

    @GetMapping(path = "{title}/{year}")
    public OmdbMovieDetails getByTitleAndYear(@PathVariable String title, @PathVariable String year){
        return omdbService.getMovieDetailsByTitleAndYear(title, year);
    }
}

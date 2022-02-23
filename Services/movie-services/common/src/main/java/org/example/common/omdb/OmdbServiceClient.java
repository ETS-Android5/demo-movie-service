package org.example.common.omdb;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        name = "omdb-service",
        url = "${common.omdb-service.url}"
)
public interface OmdbServiceClient {

    @GetMapping(path = "api/v1/omdb-service/{title}")
    OmdbMovieDetails getByTitle(@PathVariable("title") String title);

    @GetMapping(path = "api/v1/omdb-service/{title}/{year}")
    OmdbMovieDetails getByTitleAndYear(@PathVariable("title") String title, @PathVariable("year") String year);
}

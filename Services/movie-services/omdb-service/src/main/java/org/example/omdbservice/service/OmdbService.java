package org.example.omdbservice.service;

import com.google.gson.Gson;
import org.example.common.omdb.OmdbMovieDetails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class OmdbService {

    private RestTemplate restTemplate = new RestTemplate();
    private Gson gson = new Gson();

    @Value(value = "${omdbservice.api.url}")
    private String omdb_api_url;

    @Value(value = "${omdbservice.api.key}")
    private String omdb_api_key;

    public OmdbMovieDetails getMovieDetailsByTitle(String title){
        String url = String.format(omdb_api_url + "?apikey=%s&t=%s&plot=full", omdb_api_key, title);
        return getOmdbMovieDetailsFromOmdbApi(url);
    }

    public OmdbMovieDetails getMovieDetailsByTitleAndYear(String title, String year){
        String url = String.format(omdb_api_url + "?apikey=%s&t=%s&year=%s&plot=full", omdb_api_key, title, year);
        return getOmdbMovieDetailsFromOmdbApi(url);
    }

    public OmdbMovieDetails getOmdbMovieDetailsFromOmdbApi(String url){
        String res = restTemplate.getForObject(url, String.class);
        return gson.fromJson(res, OmdbMovieDetails.class);
    }
}

package co.tyrell.movievibes.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.tyrell.movievibes.model.OmdbMovieResponse;

@Service
public class MovieMetadataService {

    @Autowired
    OmdbClient omdbClient;

    public OmdbMovieResponse getMetadata(String movieTitle) {
        return omdbClient.getMovieByTitle(movieTitle);
    }

}

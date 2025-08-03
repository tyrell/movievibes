package co.tyrell.movievibes.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.tyrell.movievibes.model.OmdbMovieResponse;

@Service
public class MovieMetadataService {

    @Autowired(required = false)
    private OmdbClient omdbClient;

    @Autowired(required = false)
    private McpOmdbClient mcpOmdbClient;

    public OmdbMovieResponse getMetadata(String movieTitle) {
        // Use MCP client if available, otherwise fall back to direct API
        if (mcpOmdbClient != null) {
            return mcpOmdbClient.getMovieByTitle(movieTitle);
        } else if (omdbClient != null) {
            return omdbClient.getMovieByTitle(movieTitle);
        } else {
            throw new RuntimeException("No OMDB client available. Please configure either MCP or direct OMDB API access.");
        }
    }
}

package co.tyrell.movievibes.agent;

import co.tyrell.movievibes.model.MovieVibeRecommendationResponse;
import co.tyrell.movievibes.model.OmdbMovieResponse;
import co.tyrell.movievibes.service.MovieMetadataService;
import co.tyrell.movievibes.service.MovieVibeService;
import co.tyrell.movievibes.service.MovieRecommendationService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MovieVibeAgent {

    @Autowired
    private MovieMetadataService metadataService;

    @Autowired
    private MovieVibeService vibeService;

    @Autowired
    private MovieRecommendationService recommendationService;

    private static final Logger logger = LoggerFactory.getLogger(MovieVibeAgent.class);

    public MovieVibeRecommendationResponse fulfillGoal(String movieTitle) {

        logger.info("[Agent] Step 1: Fetching metadata for '{}'", movieTitle);
        OmdbMovieResponse movie = metadataService.getMetadata(movieTitle);

        logger.info("[Agent] Step 2: Inferring vibe from metadata...\"");
        String vibe = vibeService.inferVibe(movie);

        logger.info("[Agent] Step 3: Generating recommendations based on vibe: {}", vibe);
        List<String> recommeList = recommendationService.generateRecommendations(movie.getTitle(), vibe);

        logger.info("[Agent] Done: Compiling final response with movie title, vibe, and recommendations");

        return new MovieVibeRecommendationResponse(
                movie.getTitle(),
                vibe,
                recommeList);
    }
}

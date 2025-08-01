package co.tyrell.movievibes.agent;

import co.tyrell.movievibes.model.Movie;
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
import java.util.ArrayList;

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
        OmdbMovieResponse omdbResponse = metadataService.getMetadata(movieTitle);

        logger.info("[Agent] Step 2: Inferring vibe from metadata...\"");
        String vibe = vibeService.inferVibe(omdbResponse);

        logger.info("[Agent] Step 3: Generating recommendations based on vibe: {}", vibe);
        List<String> recommeList = recommendationService.generateRecommendations(omdbResponse.getTitle(), vibe);

        logger.info("[Agent] Step 4: Fetching metadata for recommended movies...");

        // Create Movie object from OMDb response
        Movie movie = new Movie(omdbResponse);

        // Fetch full movie data for recommendations (limit to first 5 to avoid too many API calls)
        List<Movie> recommendations = new ArrayList<>();
        int maxRecommendations = Math.min(recommeList.size(), 5);
        
        for (int i = 0; i < maxRecommendations; i++) {
            String recommendationStr = recommeList.get(i);
            // Extract title from recommendation string
            String title = extractTitleFromRecommendation(recommendationStr);
            
            try {
                logger.info("[Agent] Fetching metadata for recommendation {}/{}: '{}'", i + 1, maxRecommendations, title);
                OmdbMovieResponse recOmdbResponse = metadataService.getMetadata(title);
                
                if (recOmdbResponse != null && "True".equals(recOmdbResponse.getResponse())) {
                    // Create Movie object with full OMDb data
                    Movie recMovie = new Movie(recOmdbResponse);
                    recommendations.add(recMovie);
                    logger.info("[Agent] Successfully fetched metadata for: '{}'", title);
                } else {
                    // Fallback: create Movie with just title and description
                    Movie recMovie = new Movie();
                    recMovie.setTitle(title);
                    recMovie.setPlot(recommendationStr.contains(" - ") ? 
                        recommendationStr.split(" - ", 2)[1] : "");
                    recommendations.add(recMovie);
                    logger.warn("[Agent] Could not fetch metadata for '{}', using fallback. OMDb response: {}", 
                        title, recOmdbResponse != null ? recOmdbResponse.getError() : "null response");
                }
            } catch (Exception e) {
                // Fallback: create Movie with just title and description
                Movie recMovie = new Movie();
                recMovie.setTitle(title);
                recMovie.setPlot(recommendationStr.contains(" - ") ? 
                    recommendationStr.split(" - ", 2)[1] : "");
                recommendations.add(recMovie);
                logger.error("[Agent] Error fetching metadata for '{}': {}", title, e.getMessage());
            }
        }

        logger.info("[Agent] Done: Compiling final response with movie, vibe, and {} recommendations", recommendations.size());

        // Return new format
        return new MovieVibeRecommendationResponse(movie, vibe, recommendations);
    }

    private String extractTitleFromRecommendation(String recommendation) {
        // Extract title from strings like:
        // ". \"Bourne Identity\" - A man suffering..."
        // "1. Casino Royale - James Bond earns..."
        // "The Dark Knight - Batman must confront..."
        
        String[] parts = recommendation.split(" - ", 2);
        if (parts.length > 0) {
            String titlePart = parts[0].trim();
            
            // Remove leading numbers and dots
            titlePart = titlePart.replaceAll("^\\d+\\.\\s*", "");
            
            // Remove quotes
            titlePart = titlePart.replaceAll("^[\"']|[\"']$", "");
            
            // Clean up any remaining whitespace
            titlePart = titlePart.trim();
            
            // Handle cases where the title might have "The" at the beginning
            if (titlePart.isEmpty()) {
                // Fallback to original recommendation if extraction failed
                return recommendation.replaceAll("^\\d+\\.\\s*", "").trim();
            }
            
            return titlePart;
        }
        
        // Fallback: clean the original recommendation
        return recommendation.replaceAll("^\\d+\\.\\s*", "").replaceAll("^[\"']|[\"']$", "").trim();
    }
}

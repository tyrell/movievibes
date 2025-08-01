package co.tyrell.movievibes.model;

import java.util.List;

public record MovieVibeRecommendationResponse(
        String originalTitle,
        String vibe,
        List<String> recommendedMovies,
        Movie movie,
        String vibeAnalysis,
        List<Movie> recommendations) {

    // Constructor for backward compatibility (old format)
    public MovieVibeRecommendationResponse(String originalTitle, String vibe, List<String> recommendedMovies) {
        this(originalTitle, vibe, recommendedMovies, null, vibe, List.of());
    }

    // Constructor for new format
    public MovieVibeRecommendationResponse(Movie movie, String vibeAnalysis, List<Movie> recommendations) {
        this(movie.getTitle(), vibeAnalysis, List.of(), movie, vibeAnalysis, recommendations);
    }
}

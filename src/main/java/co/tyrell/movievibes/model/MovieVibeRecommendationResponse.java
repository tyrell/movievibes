package co.tyrell.movievibes.model;

import java.util.List;

public record MovieVibeRecommendationResponse(
        String originalTitle,
        String vibe,
        List<String> recommendedMovies) {

}

package co.tyrell.movievibes.config;

import co.tyrell.movievibes.model.OmdbMovieResponse;
import co.tyrell.movievibes.service.MovieMetadataService;
import co.tyrell.movievibes.service.MovieRecommendationService;
import co.tyrell.movievibes.service.MovieVibeService;
import co.tyrell.movievibes.service.OmdbClient;
import org.mockito.Mockito;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import java.util.Arrays;
import java.util.List;

/**
 * Test configuration that provides mock implementations of services
 * to avoid dependencies on external services like Ollama and OMDb API
 */
@TestConfiguration
public class TestConfig {

    @Bean
    @Primary
    public OllamaChatModel mockOllamaChatModel() {
        OllamaChatModel mock = Mockito.mock(OllamaChatModel.class);
        
        // Configure default response for chat model calls
        Mockito.when(mock.call(Mockito.anyString()))
                .thenReturn("Test movie vibe response");
        
        return mock;
    }

    @Bean
    @Primary
    public OmdbClient mockOmdbClient() {
        OmdbClient mock = Mockito.mock(OmdbClient.class);
        
        // Configure default response
        OmdbMovieResponse response = new OmdbMovieResponse();
        
        Mockito.when(mock.getMovieByTitle(Mockito.anyString()))
                .thenReturn(response);
        
        return mock;
    }

    @Bean
    @Primary
    public MovieVibeService mockMovieVibeService() {
        MovieVibeService mock = Mockito.mock(MovieVibeService.class);
        
        // Configure default responses for common calls
        Mockito.when(mock.inferVibe(Mockito.any(OmdbMovieResponse.class)))
                .thenReturn("Action-packed adventure with humor and heart");
        
        return mock;
    }

    @Bean
    @Primary
    public MovieRecommendationService mockMovieRecommendationService() {
        MovieRecommendationService mock = Mockito.mock(MovieRecommendationService.class);
        
        // Configure default response
        List<String> recommendations = Arrays.asList(
                "Test Movie 1 - Similar action and adventure",
                "Test Movie 2 - Comedy adventure",
                "Test Movie 3 - Action with heart"
        );
        
        Mockito.when(mock.generateRecommendations(Mockito.anyString(), Mockito.anyString()))
                .thenReturn(recommendations);
        
        return mock;
    }

    @Bean
    @Primary
    public MovieMetadataService mockMovieMetadataService() {
        MovieMetadataService mock = Mockito.mock(MovieMetadataService.class);
        
        // Configure default response
        OmdbMovieResponse response = new OmdbMovieResponse();
        
        Mockito.when(mock.getMetadata(Mockito.anyString()))
                .thenReturn(response);
        
        return mock;
    }
}

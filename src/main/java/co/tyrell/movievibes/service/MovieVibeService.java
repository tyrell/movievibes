package co.tyrell.movievibes.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.tyrell.movievibes.model.OmdbMovieResponse;

@Service
public class MovieVibeService {

    @Autowired
    OllamaChatModel chatModel;

    private static final Logger logger = LoggerFactory.getLogger(MovieVibeService.class);

    public String inferVibe(OmdbMovieResponse movie) {

        logger.info("Retrieved movie details: {}", movie.getDetails());

        String prompt = String.format("""
                Based on the following movie information, describe its overall *vibe* in one sentence.
                - Title: %s
                - Genre: %s
                - Plot: %s
                - Actors: %s
                What is the vibe of this movie?
                """, movie.getTitle(), movie.getGenre(), movie.getPlot(), movie.getActors());

        logger.info("Generated prompt to check the movie vibe: {}", prompt);
        return chatModel.call(prompt);

    }

}

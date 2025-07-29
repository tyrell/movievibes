package co.tyrell.movievibes.service;

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MovieRecommendationService {

    @Autowired
    OllamaChatModel chatModel;

    private static final Logger logger = LoggerFactory.getLogger(MovieVibeService.class);

    public List<String> generateRecommendations(String title, String vibe) {
        String prompt = String.format("""
                The following movie has this vibe:
                Title: %s
                Vibe: %s

                Recommend 3 to 5 other movies with a similar tone or vibe.
                Just list the titles, optionally with a short 1-sentence description for each.
                Return them as a plain list.
                """, title, vibe);

        logger.info("Generated prompt for recommendations: {}", prompt);
        String response = chatModel.call(prompt);

        logger.info("Received response from chat model: {}", response);
        return parseRecommendations(response);
    }

    private List<String> parseRecommendations(String response) {
        return Arrays.stream(response.split("\\r?\\n"))
                .map(line -> line.replaceAll("^[\\d\\-\\*\\s]+", "")) // Remove bullets or numbers
                .filter(line -> !line.isBlank())
                .toList();
    }

}

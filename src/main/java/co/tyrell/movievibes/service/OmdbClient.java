package co.tyrell.movievibes.service;

import co.tyrell.movievibes.model.OmdbMovieResponse;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
@RequiredArgsConstructor
@ConditionalOnProperty(name = "mcp.enabled", havingValue = "false", matchIfMissing = true)
public class OmdbClient {

    @Value("${omdb.url}")
    private String baseUrl;

    @Value("${omdb.api-key}")
    private String apiKey;

    private static final Logger logger = LoggerFactory.getLogger(OmdbClient.class);

    @PostConstruct
    public void init() {
        logger.info("OMDb URL: {}", baseUrl);
        logger.info("OMDb API Key: {}", apiKey != null ? "[PROVIDED]" : "[MISSING]");
    }

    public OmdbMovieResponse getMovieByTitle(String title) {
        logger.info("Querying OMDb for title: {}", title);
        logger.info("OMDb URL: {}", baseUrl);
        logger.info("OMDb API Key: {}", apiKey != null ? "[PROVIDED]" : "[MISSING]");

        String url = UriComponentsBuilder.fromUriString(baseUrl)
                .queryParam("apikey", apiKey)
                .queryParam("t", title)
                .build()
                .toUriString();

        logger.info("Constructed OMDb request URL: {}", url);
        logger.info("Fetching movie details for title: {}", title);

        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.getForObject(url, OmdbMovieResponse.class);
    }
}

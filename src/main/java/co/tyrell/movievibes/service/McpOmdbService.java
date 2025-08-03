package co.tyrell.movievibes.service;

import co.tyrell.movievibes.model.OmdbMovieResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Service that integrates with the OMDB MCP Server for movie metadata retrieval.
 * Provides function definitions that can be used by the LLM as tools.
 */
@Service
public class McpOmdbService {

    @Value("${mcp.omdb.base-url:http://localhost:8081}")
    private String mcpBaseUrl;

    private static final Logger logger = LoggerFactory.getLogger(McpOmdbService.class);
    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Function that can be used by the LLM to get movie information by title
     */
    public Function<MovieTitleRequest, String> getMovieByTitle() {
        return request -> {
            try {
                logger.info("Calling MCP OMDB server for movie: {}", request.title());
                
                // Call the MCP server endpoint with JSON-RPC 2.0 format
                String url = mcpBaseUrl + "/mcp";
                
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                
                // Create MCP JSON-RPC 2.0 request
                Map<String, Object> requestBody = new HashMap<>();
                requestBody.put("jsonrpc", "2.0");
                requestBody.put("id", "get-movie-" + System.currentTimeMillis());
                requestBody.put("method", "tools/call");
                
                // Create params with tool name and arguments
                Map<String, Object> params = new HashMap<>();
                params.put("name", "get_movie_details");
                params.put("arguments", Map.of("title", request.title()));
                requestBody.put("params", params);
                
                HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);
                
                ResponseEntity<String> response = restTemplate.exchange(
                    url, HttpMethod.POST, entity, String.class
                );
                
                if (response.getStatusCode() == HttpStatus.OK) {
                    logger.info("Successfully retrieved movie data from MCP server");
                    return response.getBody();
                } else {
                    logger.error("MCP server returned error: {}", response.getStatusCode());
                    return "Error retrieving movie data: " + response.getStatusCode();
                }
                
            } catch (Exception e) {
                logger.error("Error calling MCP OMDB server: {}", e.getMessage());
                return "Error retrieving movie data: " + e.getMessage();
            }
        };
    }

    /**
     * Function that can be used by the LLM to search for movies
     */
    public Function<MovieSearchRequest, String> searchMovies() {
        return request -> {
            try {
                logger.info("Searching movies via MCP OMDB server: {}", request.query());
                
                String url = mcpBaseUrl + "/mcp";
                
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                
                // Create MCP JSON-RPC 2.0 request
                Map<String, Object> requestBody = new HashMap<>();
                requestBody.put("jsonrpc", "2.0");
                requestBody.put("id", "search-movies-" + System.currentTimeMillis());
                requestBody.put("method", "tools/call");
                
                // Create params with tool name and arguments
                Map<String, Object> params = new HashMap<>();
                params.put("name", "search_movies");
                Map<String, Object> arguments = new HashMap<>();
                arguments.put("title", request.query());
                if (request.year() != null) {
                    arguments.put("year", request.year());
                }
                if (request.type() != null) {
                    arguments.put("type", request.type());
                }
                params.put("arguments", arguments);
                requestBody.put("params", params);
                
                HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);
                
                ResponseEntity<String> response = restTemplate.exchange(
                    url, HttpMethod.POST, entity, String.class
                );
                
                if (response.getStatusCode() == HttpStatus.OK) {
                    logger.info("Successfully retrieved search results from MCP server");
                    return response.getBody();
                } else {
                    logger.error("MCP server returned error: {}", response.getStatusCode());
                    return "Error searching movies: " + response.getStatusCode();
                }
                
            } catch (Exception e) {
                logger.error("Error calling MCP OMDB server for search: {}", e.getMessage());
                return "Error searching movies: " + e.getMessage();
            }
        };
    }

    /**
     * Direct method to get movie metadata (for backward compatibility)
     */
    public OmdbMovieResponse getMovieMetadata(String title) {
        try {
            Function<MovieTitleRequest, String> movieFunction = getMovieByTitle();
            String jsonResponse = movieFunction.apply(new MovieTitleRequest(title));
            
            // Parse the MCP JSON-RPC response
            JsonNode rootNode = objectMapper.readTree(jsonResponse);
            
            // Check for MCP error response
            if (rootNode.has("error") && !rootNode.path("error").isNull()) {
                JsonNode errorNode = rootNode.path("error");
                String errorMessage = errorNode.path("message").asText("Unknown MCP error");
                logger.error("MCP server returned error: {}", errorMessage);
                return createErrorResponse("MCP error: " + errorMessage);
            }
            
            // Extract result from MCP response
            JsonNode resultNode = rootNode.path("result");
            if (!resultNode.has("content")) {
                logger.error("MCP response missing content field: {}", jsonResponse);
                return createErrorResponse("Invalid MCP response format");
            }
            
            JsonNode contentNode = resultNode.path("content");
            
            // Handle content array from MCP server
            String movieDataText;
            if (contentNode.isArray() && contentNode.size() > 0) {
                JsonNode firstContent = contentNode.get(0);
                if (firstContent.path("type").asText().equals("text")) {
                    movieDataText = firstContent.path("text").asText();
                } else {
                    logger.error("Unexpected content type in MCP response: {}", firstContent.path("type").asText());
                    return createErrorResponse("Unexpected content type from MCP server");
                }
            } else {
                logger.error("MCP response content is not an array or is empty: {}", jsonResponse);
                return createErrorResponse("Invalid content format from MCP server");
            }
            
            // Try to parse as JSON first (if MCP server returns structured data)
            try {
                JsonNode movieNode = objectMapper.readTree(movieDataText);
                return parseMovieJson(movieNode);
            } catch (Exception e) {
                // If not JSON, parse the formatted text response
                return parseMovieText(movieDataText);
            }
            
        } catch (JsonProcessingException e) {
            logger.error("Error parsing JSON response from MCP server: {}", e.getMessage());
            return createErrorResponse("Error parsing response: " + e.getMessage());
        } catch (Exception e) {
            logger.error("Error getting movie metadata: {}", e.getMessage());
            return createErrorResponse("Error retrieving movie: " + e.getMessage());
        }
    }

    private OmdbMovieResponse parseMovieJson(JsonNode movieNode) {
        OmdbMovieResponse omdbResponse = new OmdbMovieResponse();
        
        // Map JSON fields to OmdbMovieResponse
        omdbResponse.setTitle(movieNode.path("Title").asText());
        omdbResponse.setYear(movieNode.path("Year").asText());
        omdbResponse.setRated(movieNode.path("Rated").asText());
        omdbResponse.setReleased(movieNode.path("Released").asText());
        omdbResponse.setRuntime(movieNode.path("Runtime").asText());
        omdbResponse.setGenre(movieNode.path("Genre").asText());
        omdbResponse.setDirector(movieNode.path("Director").asText());
        omdbResponse.setWriter(movieNode.path("Writer").asText());
        omdbResponse.setActors(movieNode.path("Actors").asText());
        omdbResponse.setPlot(movieNode.path("Plot").asText());
        omdbResponse.setLanguage(movieNode.path("Language").asText());
        omdbResponse.setCountry(movieNode.path("Country").asText());
        omdbResponse.setAwards(movieNode.path("Awards").asText());
        omdbResponse.setPoster(movieNode.path("Poster").asText());
        omdbResponse.setImdbRating(movieNode.path("imdbRating").asText());
        omdbResponse.setImdbID(movieNode.path("imdbID").asText());
        omdbResponse.setType(movieNode.path("Type").asText());
        
        // Set response status
        String responseStatus = movieNode.path("Response").asText();
        if ("True".equals(responseStatus) || !omdbResponse.getTitle().isEmpty()) {
            omdbResponse.setResponse("True");
        } else {
            omdbResponse.setResponse("False");
            omdbResponse.setError(movieNode.path("Error").asText("Movie not found"));
        }
        
        logger.info("Successfully parsed movie from JSON: {}", omdbResponse.getTitle());
        return omdbResponse;
    }

    private OmdbMovieResponse parseMovieText(String movieText) {
        // Parse formatted text response like: "🎬 The Matrix (1999)\n\nRating: R\nRuntime: 136 min\n..."
        OmdbMovieResponse omdbResponse = new OmdbMovieResponse();
        
        try {
            String[] lines = movieText.split("\\n");
            
            // Extract title and year from first line like "🎬 The Matrix (1999)"
            if (lines.length > 0) {
                String titleLine = lines[0].replaceAll("🎬\\s*", "").trim();
                if (titleLine.contains("(") && titleLine.contains(")")) {
                    int yearStart = titleLine.lastIndexOf("(");
                    int yearEnd = titleLine.lastIndexOf(")");
                    String title = titleLine.substring(0, yearStart).trim();
                    String year = titleLine.substring(yearStart + 1, yearEnd).trim();
                    omdbResponse.setTitle(title);
                    omdbResponse.setYear(year);
                } else {
                    omdbResponse.setTitle(titleLine);
                }
            }
            
            // Parse other fields from subsequent lines
            for (String line : lines) {
                line = line.trim();
                if (line.startsWith("Rating:")) {
                    omdbResponse.setRated(line.substring(7).trim());
                } else if (line.startsWith("Runtime:")) {
                    omdbResponse.setRuntime(line.substring(8).trim());
                } else if (line.startsWith("Genre:")) {
                    omdbResponse.setGenre(line.substring(6).trim());
                } else if (line.startsWith("Director:")) {
                    omdbResponse.setDirector(line.substring(9).trim());
                } else if (line.startsWith("Cast:")) {
                    omdbResponse.setActors(line.substring(5).trim());
                } else if (line.startsWith("IMDB Rating:")) {
                    String rating = line.substring(12).trim().replaceAll("/10", "");
                    omdbResponse.setImdbRating(rating);
                } else if (line.startsWith("Plot:")) {
                    omdbResponse.setPlot(line.substring(5).trim());
                }
            }
            
            // Set response as successful if we have a title
            if (omdbResponse.getTitle() != null && !omdbResponse.getTitle().isEmpty()) {
                omdbResponse.setResponse("True");
                logger.info("Successfully parsed movie from text: {}", omdbResponse.getTitle());
            } else {
                omdbResponse.setResponse("False");
                omdbResponse.setError("Could not parse movie information from response");
            }
            
        } catch (Exception e) {
            logger.error("Error parsing movie text: {}", e.getMessage());
            omdbResponse.setResponse("False");
            omdbResponse.setError("Error parsing movie text: " + e.getMessage());
        }
        
        return omdbResponse;
    }

    private OmdbMovieResponse createErrorResponse(String error) {
        OmdbMovieResponse response = new OmdbMovieResponse();
        response.setResponse("False");
        response.setError(error);
        return response;
    }

    // Record classes for function parameters
    public record MovieTitleRequest(String title) {}
    
    public record MovieSearchRequest(String query, String year, String type) {
        public MovieSearchRequest(String query) {
            this(query, null, null);
        }
    }
}

package co.tyrell.movievibes.service;

import co.tyrell.movievibes.model.OmdbMovieResponse;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * OMDB client that uses the Model Context Protocol (MCP) server to access OMDb API.
 * This service can be enabled via configuration to replace the standard OmdbClient
 * and provides standardized tool-based API access through JSON-RPC 2.0.
 */
@Service
@ConditionalOnProperty(name = "mcp.enabled", havingValue = "true")
public class McpOmdbClient {

    @Value("${mcp.omdb.base-url:http://localhost:8081}")
    private String mcpBaseUrl;

    private static final Logger logger = LoggerFactory.getLogger(McpOmdbClient.class);
    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @PostConstruct
    public void init() {
        logger.info("MCP OMDB Client enabled");
        logger.info("MCP OMDB URL: {}", mcpBaseUrl);
    }

    public OmdbMovieResponse getMovieByTitle(String title) {
        logger.info("Querying MCP OMDB server for title: {}", title);

        try {
            String url = mcpBaseUrl + "/mcp";
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            
            // Create MCP JSON-RPC 2.0 request
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("jsonrpc", "2.0");
            requestBody.put("id", "movie-search-" + System.currentTimeMillis());
            requestBody.put("method", "tools/call");
            
            // Create params with tool name and arguments
            Map<String, Object> params = new HashMap<>();
            params.put("name", "get_movie_details"); // Using the correct tool name from API docs
            params.put("arguments", Map.of("title", title));
            requestBody.put("params", params);
            
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);
            
            logger.info("Calling MCP server at: {} with request: {}", url, requestBody);
            ResponseEntity<String> response = restTemplate.exchange(
                url, HttpMethod.POST, entity, String.class
            );
            
            if (response.getStatusCode() == HttpStatus.OK) {
                return parseOmdbResponse(response.getBody());
            } else {
                logger.error("MCP server returned error: {}", response.getStatusCode());
                return createErrorResponse("MCP server error: " + response.getStatusCode());
            }
            
        } catch (Exception e) {
            logger.error("Error calling MCP OMDB server: {}", e.getMessage(), e);
            return createErrorResponse("Error calling MCP server: " + e.getMessage());
        }
    }

    private OmdbMovieResponse parseOmdbResponse(String jsonResponse) {
        try {
            logger.debug("Parsing MCP response: {}", jsonResponse);
            
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
            
            // Handle different content formats from MCP server
            String movieDataText;
            if (contentNode.isArray() && contentNode.size() > 0) {
                // Content is an array of content items
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
            
            // The text content might be formatted movie info or JSON
            // First, try to parse as JSON (in case the MCP server returns structured data)
            try {
                JsonNode movieNode = objectMapper.readTree(movieDataText);
                return parseMovieJson(movieNode);
            } catch (Exception e) {
                // If not JSON, parse the formatted text response
                return parseMovieText(movieDataText);
            }
            
        } catch (Exception e) {
            logger.error("Error parsing MCP response: {}", e.getMessage(), e);
            return createErrorResponse("Error parsing MCP response: " + e.getMessage());
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
            StringBuilder plotBuilder = new StringBuilder();
            boolean inPlotSection = false;
            
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
                    inPlotSection = true;
                    String plotStart = line.substring(5).trim();
                    if (!plotStart.isEmpty()) {
                        plotBuilder.append(plotStart);
                    }
                } else if (line.startsWith("Awards:")) {
                    inPlotSection = false;
                    omdbResponse.setAwards(line.substring(7).trim());
                } else if (line.startsWith("Released:")) {
                    omdbResponse.setReleased(line.substring(9).trim());
                } else if (line.startsWith("Writer:")) {
                    omdbResponse.setWriter(line.substring(7).trim());
                } else if (line.startsWith("Language:")) {
                    omdbResponse.setLanguage(line.substring(9).trim());
                } else if (line.startsWith("Country:")) {
                    omdbResponse.setCountry(line.substring(8).trim());
                } else if (line.startsWith("Poster:")) {
                    omdbResponse.setPoster(line.substring(7).trim());
                } else if (inPlotSection && !line.isEmpty() && !line.startsWith("Awards:")) {
                    // Continue building plot text for multi-line plots
                    if (plotBuilder.length() > 0) {
                        plotBuilder.append(" ");
                    }
                    plotBuilder.append(line);
                }
            }
            
            // Set the complete plot if we found one
            if (plotBuilder.length() > 0) {
                omdbResponse.setPlot(plotBuilder.toString().trim());
            }
            
            // Handle missing poster data - MCP server doesn't provide poster URLs
            // Set to "N/A" to match OMDB API convention for missing data
            if (omdbResponse.getPoster() == null || omdbResponse.getPoster().isEmpty()) {
                omdbResponse.setPoster("N/A");
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
}

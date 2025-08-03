package co.tyrell.movievibes.controller;

import co.tyrell.movievibes.agent.MovieVibeAgent;
import co.tyrell.movievibes.model.MovieVibeRecommendationResponse;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/agent")
public class MovieAgentController {

    private final MovieVibeAgent agent;

    public MovieAgentController(MovieVibeAgent agent) {
        this.agent = agent;
    }

    @GetMapping("/recommendations/original")
    public MovieVibeRecommendationResponse getAgenticRecommendations(@RequestParam String title) {
        return agent.fulfillGoal(title);
    }
}

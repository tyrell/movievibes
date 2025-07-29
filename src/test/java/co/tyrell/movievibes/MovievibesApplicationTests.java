package co.tyrell.movievibes;

import co.tyrell.movievibes.model.OmdbMovieResponse;
import co.tyrell.movievibes.service.OmdbClient;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class MovievibesApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@SuppressWarnings("removal")
	@MockBean
	private OmdbClient omdbMovieClient;

	@Test
	void movieVibesEndpointReturnsRecommendations() throws Exception {
		OmdbMovieResponse mockMovie = new OmdbMovieResponse();
		mockMovie.setTitle("The Matrix");
		mockMovie.setGenre("Action, Sci-Fi");
		mockMovie.setPlot("A computer hacker learns about the true nature of reality.");
		mockMovie.setActors("Keanu Reeves, Laurence Fishburne, Carrie-Anne Moss");

		Mockito.when(omdbMovieClient.getMovieByTitle("The Matrix")).thenReturn(mockMovie);

		mockMvc.perform(get("/api/agent/recommendations")
				.param("title", "The Matrix")
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.originalTitle", containsString("The Matrix")))
				.andExpect(jsonPath("$.recommendedMovies", hasSize(greaterThan(0))));
	}
}

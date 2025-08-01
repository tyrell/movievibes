package co.tyrell.movievibes;

import co.tyrell.movievibes.config.TestConfig;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
@Import(TestConfig.class)
class MovievibesApplicationTests {

	@Test
	void contextLoads() {
		// This test ensures that the Spring Boot application context loads successfully
		// It will fail if there are any configuration issues or missing dependencies
		// External services like Ollama are mocked via test configuration
	}

	@Test 
	void applicationStartsSuccessfully() {
		// This test verifies that all beans can be created and the application can start
		// without any runtime errors during initialization
		// Uses test configuration to avoid external service dependencies
	}
}

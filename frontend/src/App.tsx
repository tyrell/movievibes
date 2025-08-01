import React, { useState } from 'react';
import SearchBar from './components/SearchBar';
import MovieCard from './components/MovieCard';
import LoadingSpinner from './components/LoadingSpinner';
import { MovieService } from './services/movieService';
import { MovieVibeRecommendationResponse } from './types/movie.types';
import './App.css';

function App() {
  const [isLoading, setIsLoading] = useState(false);
  const [movieData, setMovieData] = useState<MovieVibeRecommendationResponse | null>(null);
  const [error, setError] = useState<string | null>(null);

  const handleSearch = async (title: string) => {
    setIsLoading(true);
    setError(null);
    setMovieData(null);

    try {
      const response = await MovieService.getRecommendations(title);
      
      // Validate the response structure
      if (!response) {
        throw new Error('No response received from server');
      }
      
      if (!response.movie) {
        throw new Error('Movie not found or invalid movie data received');
      }
      
      setMovieData(response);
    } catch (err) {
      console.error('Search error:', err);
      
      if (err instanceof Error) {
        setError(`Error: ${err.message}`);
      } else {
        setError('Failed to fetch movie recommendations. Please check if the backend server is running.');
      }
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <div className="app">
      {/* Header */}
      <header className="header">
        <div className="container">
          <div className="header-content">
            <h1 className="header-title">
              🎬 <span className="header-highlight">Movie Vibes</span>
            </h1>
            <p className="header-subtitle">
              Discover movies that match your vibe with AI-powered recommendations
            </p>
          </div>
        </div>
      </header>

      {/* Main Content */}
      <main className="main">
        {/* Search Section */}
        <div className="search-section">
          <SearchBar onSearch={handleSearch} isLoading={isLoading} />
        </div>

        {/* Loading State */}
        {isLoading && <LoadingSpinner />}

        {/* Error State */}
        {error && (
          <div className="error-container">
            <div className="error-title">Oops! Something went wrong</div>
            <div className="error-message">{error}</div>
            <div className="error-hint">
              Make sure your Spring Boot backend is running on port 8080
            </div>
          </div>
        )}

        {/* Results */}
        {movieData && !isLoading && (
          <div className="results-container">
            {/* Original Movie */}
            {movieData.movie && (
              <section className="section">
                <h2 className="section-title">
                  🎯 Your Movie
                </h2>
                <div className="main-movie-container">
                  <MovieCard movie={movieData.movie} isMainMovie={true} />
                </div>
              </section>
            )}

            {/* Vibe Analysis */}
            {movieData.vibeAnalysis && (
              <section className="section">
                <div className="vibe-analysis">
                  <h2 className="section-title">
                    🔮 Vibe Analysis
                  </h2>
                  <div className="vibe-content">
                    <p className="vibe-text">
                      "{movieData.vibeAnalysis}"
                    </p>
                  </div>
                </div>
              </section>
            )}

            {/* Recommendations */}
            {movieData.recommendations && movieData.recommendations.length > 0 && (
              <section className="section">
                <h2 className="section-title">
                  ✨ Recommended Movies with Similar Vibes
                </h2>
                <div className="recommendations-grid">
                  {movieData.recommendations
                    .filter(movie => movie && movie.title) // Filter out undefined/invalid movies
                    .map((movie, index) => (
                      <MovieCard key={`${movie.imdbID || movie.title}-${index}`} movie={movie} />
                    ))}
                </div>
              </section>
            )}
          </div>
        )}

        {/* Welcome State */}
        {!movieData && !isLoading && !error && (
          <div className="welcome-section">
            <div className="welcome-icon">🎬</div>
            <h2 className="welcome-title">
              Welcome to Movie Vibes!
            </h2>
            <p className="welcome-description">
              Enter any movie title above to discover its unique vibe and get personalized 
              recommendations powered by AI. Our intelligent agent analyzes movie characteristics 
              to find films that match your taste perfectly.
            </p>
            <div className="features-grid">
              <div className="feature-card">
                <div className="feature-icon">🧠</div>
                <h3 className="feature-title">AI-Powered Analysis</h3>
                <p className="feature-description">
                  Advanced algorithms analyze movie themes, genres, and vibes
                </p>
              </div>
              <div className="feature-card">
                <div className="feature-icon">🎯</div>
                <h3 className="feature-title">Personalized Recommendations</h3>
                <p className="feature-description">
                  Get tailored movie suggestions based on your preferences
                </p>
              </div>
              <div className="feature-card">
                <div className="feature-icon">⚡</div>
                <h3 className="feature-title">Instant Results</h3>
                <p className="feature-description">
                  Fast, intelligent recommendations in seconds
                </p>
              </div>
            </div>
          </div>
        )}
      </main>

      {/* Footer */}
      <footer className="footer">
        <div className="container">
          <p className="footer-text">
            Powered by Spring AI, Ollama, and React | Built with ❤️ for movie lovers
          </p>
        </div>
      </footer>
    </div>
  );
}

export default App;

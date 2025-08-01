import { MovieVibeRecommendationResponse } from '../types/movie.types';

const API_BASE_URL = process.env.REACT_APP_API_BASE_URL || 'http://localhost:8080/api';

export class MovieService {
  static async getRecommendations(title: string): Promise<MovieVibeRecommendationResponse> {
    const controller = new AbortController();
    const timeoutId = setTimeout(() => controller.abort(), 120000); // 2 minute timeout for AI operations

    try {
      const response = await fetch(
        `${API_BASE_URL}/agent/recommendations?title=${encodeURIComponent(title)}`,
        {
          method: 'GET',
          headers: {
            'Content-Type': 'application/json',
          },
          signal: controller.signal,
        }
      );

      clearTimeout(timeoutId);

      if (!response.ok) {
        const errorText = await response.text();
        throw new Error(`Server error (${response.status}): ${errorText || response.statusText}`);
      }

      const data = await response.json();
      
      // Validate response structure
      if (!data) {
        throw new Error('Empty response from server');
      }
      
      // Check if we got the new format with full movie data
      if (data.movie && data.vibeAnalysis !== undefined) {
        console.log('Received new API format with full movie data');
        return data;
      }
      
      // Check if we got the backend format (originalTitle, vibe, recommendedMovies)
      if (data.originalTitle && data.vibe) {
        console.log('Received backend API format, transforming...');
        return this.transformBackendResponse(data);
      }
      
      // Check for old frontend format
      if (!data.movie) {
        throw new Error('Invalid response: missing movie data');
      }

      return data;
    } catch (error) {
      clearTimeout(timeoutId);
      
      if (error instanceof Error && error.name === 'AbortError') {
        throw new Error('Request timeout - The AI analysis is taking longer than expected. Please try again.');
      }
      
      console.error('Error fetching movie recommendations:', error);
      throw error;
    }
  }

  // Transform backend API response format to frontend format
  private static transformBackendResponse(backendData: any): MovieVibeRecommendationResponse {
    // Create a basic movie object from the title
    const movie = {
      title: backendData.originalTitle || 'Unknown Movie',
      year: '',
      rated: '',
      released: '',
      runtime: '',
      genre: '',
      director: '',
      writer: '',
      actors: '',
      plot: '',
      language: '',
      country: '',
      awards: '',
      poster: '',
      imdbRating: '',
      imdbID: '',
      type: ''
    };

    // Parse recommended movies from strings to basic movie objects
    const recommendations = (backendData.recommendedMovies || []).map((movieStr: string, index: number) => {
      // Extract title from string like ". \"Bourne Identity\" - A man suffering..."
      const titleMatch = movieStr.match(/(?:\d+\.\s*)?["']?([^"'-]+)["']?\s*-/);
      const title = titleMatch ? titleMatch[1].trim() : `Recommendation ${index + 1}`;
      
      return {
        title: title,
        year: '',
        rated: '',
        released: '',
        runtime: '',
        genre: '',
        director: '',
        writer: '',
        actors: '',
        plot: movieStr.includes(' - ') ? movieStr.split(' - ').slice(1).join(' - ') : '',
        language: '',
        country: '',
        awards: '',
        poster: '',
        imdbRating: '',
        imdbID: '',
        type: ''
      };
    });

    return {
      movie: movie,
      vibeAnalysis: backendData.vibe || '',
      recommendations: recommendations
    };
  }
}

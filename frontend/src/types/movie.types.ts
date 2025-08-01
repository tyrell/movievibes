export interface MovieRequest {
  title: string;
}

export interface MovieVibeRecommendationResponse {
  movie: Movie;
  recommendations: Movie[];
  vibeAnalysis: string;
}

export interface Movie {
  title: string;
  year?: string;
  rated?: string;
  released?: string;
  runtime?: string;
  genre?: string;
  director?: string;
  writer?: string;
  actors?: string;
  plot?: string;
  language?: string;
  country?: string;
  awards?: string;
  poster?: string;
  imdbRating?: string;
  imdbID?: string;
  type?: string;
}

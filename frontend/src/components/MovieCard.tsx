import React from 'react';
import { Movie } from '../types/movie.types';

interface MovieCardProps {
  movie: Movie;
  isMainMovie?: boolean;
}

const MovieCard: React.FC<MovieCardProps> = ({ movie, isMainMovie = false }) => {
  // Add defensive check for undefined movie
  if (!movie) {
    return (
      <div className="movie-card">
        <div className="movie-card-content">
          <div className="movie-poster-placeholder">
            <span className="no-image-text">Loading...</span>
          </div>
        </div>
      </div>
    );
  }

  const cardClasses = isMainMovie 
    ? "movie-card movie-card-main"
    : "movie-card movie-card-recommendation";

  return (
    <div className={cardClasses}>
      <div className="movie-card-content">
        {movie.poster && movie.poster !== "N/A" ? (
          <img
            src={movie.poster}
            alt={`${movie.title} poster`}
            className={`movie-poster ${isMainMovie ? 'main' : 'recommendation'}`}
            onError={(e) => {
              (e.target as HTMLImageElement).src = 'https://via.placeholder.com/300x450/374151/f3f4f6?text=No+Image';
            }}
          />
        ) : (
          <div className={`movie-poster-placeholder ${isMainMovie ? 'main' : 'recommendation'}`}>
            <span className="no-image-text">No Image</span>
          </div>
        )}
        
        <div className="movie-info">
          <h3 className={`movie-title ${isMainMovie ? 'main' : 'recommendation'}`}>
            {movie.title}
          </h3>
          
          <div className="movie-metadata">
            {movie.year && <span>{movie.year}</span>}
            {movie.year && movie.rated && <span>•</span>}
            {movie.rated && <span>{movie.rated}</span>}
            {movie.imdbRating && movie.imdbRating !== "N/A" && (
              <>
                <span>•</span>
                <span className="rating">⭐ {movie.imdbRating}</span>
              </>
            )}
          </div>
          
          {movie.genre && <p className="movie-genre">{movie.genre}</p>}
          
          {isMainMovie && (
            <div className="movie-details">
              {movie.plot && <p className="movie-plot">{movie.plot}</p>}
              {movie.director && (
                <p className="movie-director">
                  <strong>Director:</strong> {movie.director}
                </p>
              )}
              {movie.actors && (
                <p className="movie-actors">
                  <strong>Starring:</strong> {movie.actors}
                </p>
              )}
            </div>
          )}
        </div>
      </div>
    </div>
  );
};

export default MovieCard;

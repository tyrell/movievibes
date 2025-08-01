import React, { useState } from 'react';

interface SearchBarProps {
  onSearch: (title: string) => void;
  isLoading: boolean;
}

const SearchBar: React.FC<SearchBarProps> = ({ onSearch, isLoading }) => {
  const [searchTitle, setSearchTitle] = useState('');

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    if (searchTitle.trim()) {
      onSearch(searchTitle.trim());
    }
  };

  return (
    <div className="search-bar-container">
      <form onSubmit={handleSubmit} className="search-form">
        <div className="search-input-wrapper">
          <input
            type="text"
            value={searchTitle}
            onChange={(e) => setSearchTitle(e.target.value)}
            placeholder="Enter a movie title to get recommendations..."
            className="search-input"
            disabled={isLoading}
          />
          <button
            type="submit"
            disabled={isLoading || !searchTitle.trim()}
            className={`search-button ${isLoading || !searchTitle.trim() ? 'disabled' : ''}`}
          >
            {isLoading ? (
              <div className="search-spinner"></div>
            ) : (
              <svg className="search-icon" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z" />
              </svg>
            )}
          </button>
        </div>
      </form>
      
      {isLoading && (
        <div className="search-loading">
          <p className="search-loading-text">
            Analyzing movie vibes and generating recommendations...
          </p>
        </div>
      )}
    </div>
  );
};

export default SearchBar;

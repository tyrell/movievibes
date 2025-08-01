import React from 'react';

const LoadingSpinner: React.FC = () => {
  return (
    <div className="loading-spinner-container">
      <div className="spinner-wrapper">
        <div className="spinner-outer"></div>
        <div className="spinner-inner"></div>
      </div>
      <p className="loading-text">
        Analyzing movie vibes with AI...
      </p>
      <p className="loading-subtext">
        Please wait, this process can take 30-60 seconds while our AI agent analyzes the movie and generates recommendations ✨
      </p>
      <p className="loading-subtext" style={{ fontSize: '12px', marginTop: '8px', color: '#9ca3af' }}>
        💡 Tip: The backend is processing your request with a local AI model
      </p>
    </div>
  );
};

export default LoadingSpinner;

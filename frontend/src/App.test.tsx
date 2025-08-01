import React from 'react';
import { render, screen } from '@testing-library/react';
import App from './App';

test('renders Movie Vibes header', () => {
  render(<App />);
  const headerElement = screen.getByRole('heading', { level: 1 });
  expect(headerElement).toHaveTextContent('Movie Vibes');
});

test('renders search input', () => {
  render(<App />);
  const searchInput = screen.getByPlaceholderText(/Enter a movie title to get recommendations/i);
  expect(searchInput).toBeInTheDocument();
});

test('renders welcome message', () => {
  render(<App />);
  const welcomeElement = screen.getByText(/Welcome to Movie Vibes!/i);
  expect(welcomeElement).toBeInTheDocument();
});

package co.tyrell.movievibes.model;

public class Movie {
    private String title;
    private String year;
    private String rated;
    private String released;
    private String runtime;
    private String genre;
    private String director;
    private String writer;
    private String actors;
    private String plot;
    private String language;
    private String country;
    private String awards;
    private String poster;
    private String imdbRating;
    private String imdbID;
    private String type;

    // Default constructor
    public Movie() {}

    // Constructor from OmdbMovieResponse
    public Movie(OmdbMovieResponse omdbResponse) {
        this.title = omdbResponse.getTitle();
        this.year = omdbResponse.getYear();
        this.rated = omdbResponse.getRated();
        this.released = omdbResponse.getReleased();
        this.runtime = omdbResponse.getRuntime();
        this.genre = omdbResponse.getGenre();
        this.director = omdbResponse.getDirector();
        this.writer = omdbResponse.getWriter();
        this.actors = omdbResponse.getActors();
        this.plot = omdbResponse.getPlot();
        this.language = omdbResponse.getLanguage();
        this.country = omdbResponse.getCountry();
        this.awards = omdbResponse.getAwards();
        this.poster = omdbResponse.getPoster();
        this.imdbRating = omdbResponse.getImdbRating();
        this.imdbID = omdbResponse.getImdbID();
        this.type = omdbResponse.getType();
    }

    // Getters and setters
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getYear() { return year; }
    public void setYear(String year) { this.year = year; }

    public String getRated() { return rated; }
    public void setRated(String rated) { this.rated = rated; }

    public String getReleased() { return released; }
    public void setReleased(String released) { this.released = released; }

    public String getRuntime() { return runtime; }
    public void setRuntime(String runtime) { this.runtime = runtime; }

    public String getGenre() { return genre; }
    public void setGenre(String genre) { this.genre = genre; }

    public String getDirector() { return director; }
    public void setDirector(String director) { this.director = director; }

    public String getWriter() { return writer; }
    public void setWriter(String writer) { this.writer = writer; }

    public String getActors() { return actors; }
    public void setActors(String actors) { this.actors = actors; }

    public String getPlot() { return plot; }
    public void setPlot(String plot) { this.plot = plot; }

    public String getLanguage() { return language; }
    public void setLanguage(String language) { this.language = language; }

    public String getCountry() { return country; }
    public void setCountry(String country) { this.country = country; }

    public String getAwards() { return awards; }
    public void setAwards(String awards) { this.awards = awards; }

    public String getPoster() { return poster; }
    public void setPoster(String poster) { this.poster = poster; }

    public String getImdbRating() { return imdbRating; }
    public void setImdbRating(String imdbRating) { this.imdbRating = imdbRating; }

    public String getImdbID() { return imdbID; }
    public void setImdbID(String imdbID) { this.imdbID = imdbID; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
}

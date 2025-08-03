package co.tyrell.movievibes.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class OmdbMovieResponse {
    @JsonProperty("Title")
    private String title;

    @JsonProperty("Year")
    private String year;

    @JsonProperty("Genre")
    private String genre;

    @JsonProperty("Plot")
    private String plot;

    @JsonProperty("Actors")
    private String actors;

    @JsonProperty("Poster")
    private String poster;

    @JsonProperty("Director")
    private String director;

    @JsonProperty("Writer")
    private String writer;

    @JsonProperty("Rated")
    private String rated;

    @JsonProperty("Released")
    private String released;

    @JsonProperty("Runtime")
    private String runtime;

    @JsonProperty("Language")
    private String language;

    @JsonProperty("Country")
    private String country;

    @JsonProperty("Awards")
    private String awards;

    @JsonProperty("imdbRating")
    private String imdbRating;

    @JsonProperty("imdbID")
    private String imdbID;

    @JsonProperty("Type")
    private String type;

    @JsonProperty("Response")
    private String response;

    @JsonProperty("Error")
    private String error;

    public void setTitle(String title) {
        this.title = title;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public void setPlot(String plot) {
        this.plot = plot;
    }

    public void setActors(String actors) {
        this.actors = actors;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public void setWriter(String writer) {
        this.writer = writer;
    }

    public void setRated(String rated) {
        this.rated = rated;
    }

    public void setReleased(String released) {
        this.released = released;
    }

    public void setRuntime(String runtime) {
        this.runtime = runtime;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setAwards(String awards) {
        this.awards = awards;
    }

    public void setImdbRating(String imdbRating) {
        this.imdbRating = imdbRating;
    }

    public void setImdbID(String imdbID) {
        this.imdbID = imdbID;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getTitle() {
        return title;
    }

    public String getYear() {
        return year;
    }

    public String getGenre() {
        return genre;
    }

    public String getPlot() {
        return plot;
    }

    public String getActors() {
        return actors;
    }

    public String getPoster() {
        return poster;
    }

    public String getDirector() {
        return director;
    }

    public String getWriter() {
        return writer;
    }

    public String getRated() {
        return rated;
    }

    public String getReleased() {
        return released;
    }

    public String getRuntime() {
        return runtime;
    }

    public String getLanguage() {
        return language;
    }

    public String getCountry() {
        return country;
    }

    public String getAwards() {
        return awards;
    }

    public String getImdbRating() {
        return imdbRating;
    }

    public String getImdbID() {
        return imdbID;
    }

    public String getType() {
        return type;
    }

    public String getResponse() {
        return response;
    }

    public String getError() {
        return error;
    }

    public String getDetails() {
        return "OmdbMovieResponse{" +
                "title='" + title + '\'' +
                ", year='" + year + '\'' +
                ", genre='" + genre + '\'' +
                ", plot='" + plot + '\'' +
                ", actors='" + actors + '\'' +
                ", response='" + response + '\'' +
                ", error='" + error + '\'' +
                '}';
    }
}

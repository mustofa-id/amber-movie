package id.mustofa.app.amber.data.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;

/**
 * @author Habib Mustofa
 * Indonesia on 11/07/19.
 */
@Entity(tableName = "favorite_movie")
public class MovieFavorite extends Movie {
  
  private String type;
  
  public MovieFavorite() {}
  
  @Ignore
  public MovieFavorite(Movie movie) {
    this.setId(movie.getId());
    this.setTitle(movie.getTitle());
    this.setOriginalTitle(movie.getOriginalTitle());
    this.setPosterPath(movie.getPosterPath());
    this.setBackdropPath(movie.getBackdropPath());
    this.setReleaseDate(movie.getReleaseDate());
    this.setPopularity(movie.getPopularity());
    this.setVoteCount(movie.getVoteCount());
    this.setVoteAverage(movie.getVoteAverage());
    this.setOverview(movie.getOverview());
    this.setOriginalLanguage(movie.getOriginalLanguage());
    this.setOriginCountry(movie.getOriginCountry());
    this.setGenreIds(movie.getGenreIds());
    this.setAdult(movie.isAdult());
  }
  
  public void setType(String type) {
    this.type = type;
  }
  
  public String getType() {
    return type;
  }
}

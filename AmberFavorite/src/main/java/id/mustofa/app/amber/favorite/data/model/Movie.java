package id.mustofa.app.amber.favorite.data.model;

import android.database.Cursor;
import android.net.Uri;

import static id.mustofa.app.amber.favorite.data.DataContract.COLUMN;
import static id.mustofa.app.amber.favorite.data.DataContract.URI_MOVIE;
import static id.mustofa.app.amber.favorite.data.DataContract.getColumn;

/**
 * @author Habib Mustofa
 * Indonesia on 21/07/19.
 */
public class Movie {
  
  private long id;
  private String title;
  private String originalTitle;
  private String posterPath;
  private String backdropPath;
  private String releaseDate;
  private float popularity;
  private int voteCount;
  private float voteAverage;
  private String overview;
  private String originalLanguage;
  private String originCountry;
  private String genreIds;
  private boolean adult;
  private String type;
  
  public Movie(Cursor cursor) {
    this.id = getColumn(Long.class, cursor, COLUMN.ID);
    this.title = getColumn(String.class, cursor, COLUMN.TITLE);
    this.originalTitle = getColumn(String.class, cursor, COLUMN.ORIGINAL_TITLE);
    this.posterPath = getColumn(String.class, cursor, COLUMN.POSTER_PATH);
    this.backdropPath = getColumn(String.class, cursor, COLUMN.BACKDROP_PATH);
    this.releaseDate = getColumn(String.class, cursor, COLUMN.RELEASE_DATE);
    this.popularity = getColumn(Float.class, cursor, COLUMN.POPULARITY);
    this.voteCount = getColumn(Integer.class, cursor, COLUMN.VOTE_COUNT);
    this.voteAverage = getColumn(Float.class, cursor, COLUMN.VOTE_AVERAGE);
    this.overview = getColumn(String.class, cursor, COLUMN.OVERVIEW);
    this.originalLanguage = getColumn(String.class, cursor, COLUMN.ORIGINAL_LANG);
    this.originCountry = getColumn(String.class, cursor, COLUMN.ORIGIN_COUNTRY);
    this.genreIds = getColumn(String.class, cursor, COLUMN.GENRE_IDS);
    this.adult = getColumn(Boolean.class, cursor, COLUMN.ADULT);
    this.type = getColumn(String.class, cursor, COLUMN.TYPE);
  }
  
  public long getId() {
    return id;
  }
  
  public String getTitle() {
    return title;
  }
  
  public String getOriginalTitle() {
    return originalTitle;
  }
  
  public String getPosterPath() {
    return posterPath;
  }
  
  public String getBackdropPath() {
    return backdropPath;
  }
  
  public String getReleaseDate() {
    return releaseDate;
  }
  
  public float getPopularity() {
    return popularity;
  }
  
  public int getVoteCount() {
    return voteCount;
  }
  
  public float getVoteAverage() {
    return voteAverage;
  }
  
  public String getOverview() {
    return overview;
  }
  
  public String getOriginalLanguage() {
    return originalLanguage;
  }
  
  public String getOriginCountry() {
    return originCountry;
  }
  
  public String getGenreIds() {
    return genreIds;
  }
  
  public boolean isAdult() {
    return adult;
  }
  
  public String getType() {
    return type;
  }
  
  public Uri getUri() {
    return Uri.parse(URI_MOVIE + "/" + id);
  }
}

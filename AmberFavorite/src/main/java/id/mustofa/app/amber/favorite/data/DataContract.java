package id.mustofa.app.amber.favorite.data;

import android.database.Cursor;
import android.net.Uri;

/**
 * @author Habib Mustofa
 * Indonesia on 21/07/19.
 */
public class DataContract {
  
  private static final String SCHEME = "content";
  private static final String AUTHORITY = "id.mustofa.app.amber.provider";
  private static final String TABLE_NAME = "favorite_movie";
  
  public static final Uri URI_MOVIE = new Uri.Builder()
      .scheme(SCHEME)
      .authority(AUTHORITY)
      .appendPath(TABLE_NAME)
      .build();
  
  @SuppressWarnings("unchecked")
  public static <T> T getColumn(Class<T> type, Cursor cursor, String name) {
    int index = cursor.getColumnIndex(name);
    if (type.isAssignableFrom(String.class)) {
      return (T) cursor.getString(index);
    } else if (type.isAssignableFrom(Integer.class)) {
      return (T) (Integer) cursor.getInt(index);
    } else if (type.isAssignableFrom(Long.class)) {
      return (T) (Long) cursor.getLong(index);
    } else if (type.isAssignableFrom(Float.class)) {
      return (T) (Float) cursor.getFloat(index);
    } else if (type.isAssignableFrom(Boolean.class)) {
      return (T) (Boolean) (cursor.getInt(index) != 0);
    }
    throw new RuntimeException("Type not supported: " + type.getName());
  }
  
  public interface COLUMN {
    String ID = "id";
    String TITLE = "title";
    String ORIGINAL_TITLE = "originalTitle";
    String POSTER_PATH = "posterPath";
    String BACKDROP_PATH = "backdropPath";
    String RELEASE_DATE = "releaseDate";
    String POPULARITY = "popularity";
    String VOTE_COUNT = "voteCount";
    String VOTE_AVERAGE = "voteAverage";
    String OVERVIEW = "overview";
    String ORIGINAL_LANG = "originalLanguage";
    String ORIGIN_COUNTRY = "originCountry";
    String GENRE_IDS = "genreIds";
    String ADULT = "adult";
    String TYPE = "type";
  }
}

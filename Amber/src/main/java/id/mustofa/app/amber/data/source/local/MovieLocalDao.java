package id.mustofa.app.amber.data.source.local;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.database.Cursor;

import java.util.List;

import id.mustofa.app.amber.data.model.Movie;

/**
 * @author Habib Mustofa
 * Indonesia on 11/07/19.
 */
@Dao
public interface MovieLocalDao {
  
  @Query("SELECT * FROM favorite_movie WHERE type=:type")
  List<Movie> findFavoriteByType(String type);
  
  @Query("SELECT * FROM favorite_movie WHERE title LIKE :query")
  List<Movie> searchFavorite(String query);
  
  @Query("SELECT COUNT(*) FROM favorite_movie WHERE id=:id")
  int countFavoriteById(long id);
  
  @Insert
  long insertFavorite(Movie movie);
  
  @Delete
  int deleteFavorite(Movie movie);
  
  // ContentProvider support
  @Query("SELECT * FROM favorite_movie")
  Cursor selectAllFavorites();
  
  @Query("SELECT * FROM favorite_movie WHERE id=:id")
  Cursor selectFavoriteById(long id);
  
  @Query("DELETE FROM favorite_movie WHERE id=:id")
  int deleteFavoriteById(long id);
}

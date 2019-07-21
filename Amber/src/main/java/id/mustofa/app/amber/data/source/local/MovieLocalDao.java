package id.mustofa.app.amber.data.source.local;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.database.Cursor;

import java.util.List;

import id.mustofa.app.amber.data.model.MovieFavorite;

/**
 * @author Habib Mustofa
 * Indonesia on 11/07/19.
 */
@Dao
public interface MovieLocalDao {
  
  @Query("SELECT * FROM favorite_movie WHERE type=:type")
  List<MovieFavorite> findFavoriteByType(String type);
  
  @Query("SELECT COUNT(*) FROM favorite_movie WHERE id=:id")
  int countFavoriteById(long id);
  
  @Insert
  long insertFavorite(MovieFavorite movie);
  
  @Delete
  int deleteFavorite(MovieFavorite movie);
  
  // ContentProvider support
  @Query("SELECT * FROM favorite_movie")
  Cursor selectAllFavorites();
  
  @Query("SELECT * FROM favorite_movie WHERE id=:id")
  Cursor selectFavoriteById(long id);
  
  @Query("DELETE FROM favorite_movie WHERE id=:id")
  int deleteFavoriteById(long id);
}

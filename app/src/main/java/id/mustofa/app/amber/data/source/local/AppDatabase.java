package id.mustofa.app.amber.data.source.local;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;

import id.mustofa.app.amber.data.model.MovieFavorite;
import id.mustofa.app.amber.util.DataConverter;

/**
 * @author Habib Mustofa
 * Indonesia on 11/07/19.
 */
@Database(entities = {MovieFavorite.class}, version = 1, exportSchema = false)
@TypeConverters({DataConverter.class})
public abstract class AppDatabase extends RoomDatabase {
  
  private static final String DATABASE_NAME = "movie.db";
  private static AppDatabase sInstance;
  
  public static AppDatabase getInstance(Context context) {
    if (sInstance == null) {
      synchronized (AppDatabase.class) {
        if (sInstance == null) {
          sInstance = Room.databaseBuilder(
              context.getApplicationContext(),
              AppDatabase.class,
              DATABASE_NAME
          ).build();
        }
      }
    }
    return sInstance;
  }
  
  public abstract MovieLocalDao movieLocalDao();
}

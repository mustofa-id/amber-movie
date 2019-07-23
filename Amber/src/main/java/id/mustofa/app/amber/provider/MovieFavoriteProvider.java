package id.mustofa.app.amber.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import id.mustofa.app.amber.data.source.local.AppDatabase;
import id.mustofa.app.amber.data.source.local.MovieLocalDao;

/**
 * @author Habib Mustofa
 * Indonesia on 21/07/19.
 */
public class MovieFavoriteProvider extends ContentProvider {
  
  public static final String AUTHORITY = "id.mustofa.app.amber.provider";
  
  private static final String TABLE_NAME = "favorite_movie";
  private static final int MOVIE_FAVORITE_DIR = 1;
  private static final int MOVIE_FAVORITE_ITEM = 2;
  private static final UriMatcher MATCHER = new UriMatcher(UriMatcher.NO_MATCH);
  
  static {
    MATCHER.addURI(AUTHORITY, TABLE_NAME, MOVIE_FAVORITE_DIR);
    MATCHER.addURI(AUTHORITY, TABLE_NAME + "/*", MOVIE_FAVORITE_ITEM);
  }
  
  @Override
  public boolean onCreate() {
    return true;
  }
  
  @Nullable
  @Override
  public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection,
                      @Nullable String[] selectionArgs, @Nullable String sortOrder) {
  
    final Context context = getContext();
    if (context == null) {
      return null;
    }
  
    final int uriPath = MATCHER.match(uri);
    final MovieLocalDao dao = AppDatabase.getInstance(context).movieLocalDao();
  
    Cursor cursor;
    if (uriPath == MOVIE_FAVORITE_DIR) {
      cursor = dao.selectAllFavorites();
    } else if (uriPath == MOVIE_FAVORITE_ITEM) {
      cursor = dao.selectFavoriteById(ContentUris.parseId(uri));
    } else {
      throw new IllegalArgumentException("Unknown URI: " + uri);
    }
  
    cursor.setNotificationUri(context.getContentResolver(), uri);
    return cursor;
  }
  
  @Nullable
  @Override
  public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
    throw new IllegalAccessError("Insert not allowed");
  }
  
  @Override
  public int delete(@NonNull Uri uri,
                    @Nullable String selection, @Nullable String[] selectionArgs) {
    switch (MATCHER.match(uri)) {
      case MOVIE_FAVORITE_DIR:
        throw new IllegalArgumentException("Invalid URI, cannot delete without ID" + uri);
      case MOVIE_FAVORITE_ITEM:
        final Context context = getContext();
        if (context == null) {
          return 0;
        }
        final MovieLocalDao dao = AppDatabase.getInstance(context).movieLocalDao();
        final int delete = dao.deleteFavoriteById(ContentUris.parseId(uri));
        context.getContentResolver().notifyChange(uri, null);
        return delete;
      default:
        throw new IllegalArgumentException("Unknown URI: " + uri);
    }
  }
  
  @Override
  public int update(@NonNull Uri uri, @Nullable ContentValues values,
                    @Nullable String selection, @Nullable String[] selectionArgs) {
    throw new IllegalAccessError("Update not allowed");
  }
  
  @Nullable
  @Override
  public String getType(@NonNull Uri uri) {
    final int uriPath = MATCHER.match(uri);
    final String format;
    if (uriPath == MOVIE_FAVORITE_DIR) {
      format = "vnd.android.cursor.dir/%s.%s";
    } else if (uriPath == MOVIE_FAVORITE_ITEM) {
      format = "vnd.android.cursor.item/%s.%s";
    } else {
      throw new IllegalArgumentException("Unknown URI: " + uri);
    }
    return String.format(format, AUTHORITY, TABLE_NAME);
  }
}

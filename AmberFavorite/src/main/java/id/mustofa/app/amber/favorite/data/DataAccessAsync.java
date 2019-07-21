package id.mustofa.app.amber.favorite.data;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import id.mustofa.app.amber.favorite.data.model.Movie;

/**
 * @author Habib Mustofa
 * Indonesia on 21/07/19.
 */
public class DataAccessAsync {
  
  public interface Callback<T> {
    void onResult(T data);
  }
  
  public static class GetMovies extends Task<Void, List<Movie>> {
    
    public GetMovies(ContentResolver resolver, Callback<List<Movie>> callback) {
      super(resolver, callback);
    }
    
    @Override
    protected List<Movie> doInBackground(Void... voids) {
      final List<Movie> result = new ArrayList<>();
      final ContentResolver resolver = mContentResolver.get();
      final Cursor cursor = resolver.query(DataContract.URI_MOVIE, null, null, null, null);
      if (cursor != null) {
        while (cursor.moveToNext()) {
          result.add(new Movie(cursor));
        }
        cursor.close();
      }
      return result;
    }
  }
  
  public static class GetMovie extends Task<Uri, Movie> {
    
    public GetMovie(ContentResolver resolver, Callback<Movie> callback) {
      super(resolver, callback);
    }
    
    @Override
    protected Movie doInBackground(Uri... uris) {
      if (uris == null || uris.length == 0) {
        return null;
      }
      final ContentResolver resolver = mContentResolver.get();
      final Cursor cursor = resolver.query(uris[0], null, null, null, null);
      Movie movie = null;
      if (cursor != null) {
        if (cursor.moveToFirst()) {
          movie = new Movie(cursor);
        }
        cursor.close();
      }
      return movie;
    }
  }
  
  public static class RemoveMovie extends Task<Uri, Integer> {
    
    public RemoveMovie(ContentResolver resolver, Callback<Integer> callback) {
      super(resolver, callback);
    }
    
    @Override
    protected Integer doInBackground(Uri... uris) {
      if (uris == null || uris.length == 0) {
        return null;
      }
      final ContentResolver resolver = mContentResolver.get();
      return resolver.delete(uris[0], null, null);
    }
  }
  
  private abstract static class Task<P, R> extends AsyncTask<P, Void, R> {
    
    final WeakReference<ContentResolver> mContentResolver;
    final WeakReference<Callback<R>> mCallback;
    
    Task(ContentResolver resolver, Callback<R> callback) {
      this.mContentResolver = new WeakReference<>(resolver);
      this.mCallback = new WeakReference<>(callback);
    }
    
    @Override
    protected void onPostExecute(R r) {
      super.onPostExecute(r);
      mCallback.get().onResult(r);
    }
  }
}

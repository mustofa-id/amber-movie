package id.mustofa.app.amber.data;

import android.support.annotation.NonNull;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import id.mustofa.app.amber.data.model.Genre;
import id.mustofa.app.amber.data.model.MediaType;
import id.mustofa.app.amber.data.model.Movie;
import id.mustofa.app.amber.data.model.MovieFavorite;
import id.mustofa.app.amber.data.model.MovieWrapper;
import id.mustofa.app.amber.data.source.local.MovieLocalDao;
import id.mustofa.app.amber.data.source.remote.MovieRemoteDao;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @author Habib Mustofa
 * Indonesia on 06/07/19.
 */
public final class MovieRepository {
  
  private static final String TAG = MovieRepository.class.getName();
  
  private final MovieRemoteDao mMovieRemoteDao;
  private final MovieLocalDao mMovieLocalDao;
  
  public MovieRepository(MovieRemoteDao mMovieRemoteDao, MovieLocalDao mMovieLocalDao) {
    this.mMovieRemoteDao = mMovieRemoteDao;
    this.mMovieLocalDao = mMovieLocalDao;
  }
  
  public void findMovies(@NonNull MediaType mediaType,
                         Map<String, String> params,
                         @NonNull ResultListener<List<Movie>> listener) {
    final List<Movie> movies = new ArrayList<>();
    final Call<MovieWrapper> serviceMovies = mMovieRemoteDao.getDiscovers(mediaType.getValue(), params);
    
    serviceMovies.enqueue(new Callback<MovieWrapper>() {
      @Override
      public void onResponse(@NonNull Call<MovieWrapper> call, @NonNull Response<MovieWrapper> response) {
        final MovieWrapper movieWrapper = response.body();
        if (movieWrapper != null) {
          final List<Movie> results = movieWrapper.getResults();
          if (results != null) movies.addAll(results);
        }
        listener.onResult(movies, null);
      }
      
      @Override
      public void onFailure(@NonNull Call<MovieWrapper> call, @NonNull Throwable t) {
        Log.wtf(TAG, "onFailure: ", t);
        listener.onResult(movies, t);
      }
    });
  }
  
  public void findMoviesByQuery(@NonNull MediaType mediaType,
                                Map<String, String> params,
                                @NonNull ResultListener<List<Movie>> listener) {
    final List<Movie> movies = new ArrayList<>();
    final Call<MovieWrapper> serviceMovies = mMovieRemoteDao.searchMovies(mediaType.getValue(), params);
    
    serviceMovies.enqueue(new Callback<MovieWrapper>() {
      @Override
      public void onResponse(@NonNull Call<MovieWrapper> call, @NonNull Response<MovieWrapper> response) {
        final MovieWrapper movieWrapper = response.body();
        if (movieWrapper != null) {
          final List<Movie> results = movieWrapper.getResults();
          if (results != null) movies.addAll(results);
        }
        listener.onResult(movies, null);
      }
      
      @Override
      public void onFailure(@NonNull Call<MovieWrapper> call, @NonNull Throwable t) {
        Log.wtf(TAG, "onFailure: ", t);
        listener.onResult(movies, t);
      }
    });
  }
  
  public void findGenres(@NonNull MediaType type, @NonNull ResultListener<List<Genre>> listener) {
    final List<Genre> genres = new ArrayList<>();
    final Call<Genre.Wrapper> serviceGenres = mMovieRemoteDao.getGenres(type.getValue());
    
    serviceGenres.enqueue(new Callback<Genre.Wrapper>() {
      @Override
      public void onResponse(@NonNull Call<Genre.Wrapper> call, @NonNull Response<Genre.Wrapper> response) {
        final Genre.Wrapper body = response.body();
        if (body != null) genres.addAll(body.genres);
        listener.onResult(genres, null);
      }
      
      @Override
      public void onFailure(@NonNull Call<Genre.Wrapper> call, @NonNull Throwable t) {
        Log.wtf(TAG, "onFailure: ", t);
        listener.onResult(genres, t);
      }
    });
  }
  
  public void findFavoriteMovies(@NonNull MediaType type, @NonNull ResultListener<List<MovieFavorite>> listener) {
    List<MovieFavorite> favoriteList = new ArrayList<>();
    execute(favoriteList, listener, () -> {
      List<MovieFavorite> favorites = mMovieLocalDao.findFavoriteByType(type.getValue());
      favoriteList.addAll(favorites);
      listener.onResult(favoriteList, null);
    });
  }
  
  public void isInFavorite(long movieId, @NonNull ResultListener<Boolean> listener) {
    execute(false, listener, () -> {
      int count = mMovieLocalDao.countFavoriteById(movieId);
      listener.onResult(count > 0, null);
    });
  }
  
  public void addToFavorite(@NonNull MovieFavorite movie, @NonNull ResultListener<Long> listener) {
    execute(-1L, listener, () -> {
      long result = mMovieLocalDao.insertFavorite(movie);
      listener.onResult(result, null);
    });
  }
  
  public void removeFromFavorite(@NonNull MovieFavorite movie, @NonNull ResultListener<Integer> listener) {
    execute(-1, listener, () -> {
      int result = mMovieLocalDao.deleteFavorite(movie);
      listener.onResult(result, null);
    });
  }
  
  private <T> void execute(T failResult, ResultListener<T> listener, Runnable runnable) {
    try {
      final ExecutorService service = Executors.newSingleThreadExecutor();
      service.execute(runnable);
    } catch (Exception e) {
      Log.wtf(TAG, "execute: ", e);
      listener.onResult(failResult, e);
    }
  }
}

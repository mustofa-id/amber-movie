package id.mustofa.app.amber.data;

import android.support.annotation.NonNull;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import id.mustofa.app.amber.data.source.remote.ApiClient;
import id.mustofa.app.amber.data.source.RemoteDataSource;
import id.mustofa.app.amber.data.model.Genre;
import id.mustofa.app.amber.data.model.MediaType;
import id.mustofa.app.amber.data.model.Movie;
import id.mustofa.app.amber.data.model.MovieWrapper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @author Habib Mustofa
 * Indonesia on 06/07/19.
 */
public final class MovieRepository {
  
  private static final String TAG = MovieRepository.class.getName();
  
  public void findMovies(@NonNull MediaType mediaType, @NonNull ResultListener<List<Movie>> listener) {
    final List<Movie> movies = new ArrayList<>();
    final RemoteDataSource remoteDataSource = ApiClient.retrofit().create(RemoteDataSource.class);
    final Call<MovieWrapper> serviceMovies = remoteDataSource.getDiscovers(mediaType.getValue());
    
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
    final RemoteDataSource remoteDataSource = ApiClient.retrofit().create(RemoteDataSource.class);
    final Call<Genre.Wrapper> serviceGenres = remoteDataSource.getGenres(type.getValue());
    
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
}

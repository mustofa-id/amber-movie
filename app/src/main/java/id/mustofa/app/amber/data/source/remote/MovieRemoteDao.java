package id.mustofa.app.amber.data.source.remote;

import id.mustofa.app.amber.BuildConfig;
import id.mustofa.app.amber.data.model.Genre;
import id.mustofa.app.amber.data.model.MovieWrapper;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * @author Habib Mustofa
 * Indonesia on 06/07/19.
 */
public interface MovieRemoteDao {
  
  @GET("discover/{type}?language=en-US&api_key=" + BuildConfig.MOVIEDB_API_KEY)
  Call<MovieWrapper> getDiscovers(@Path("type") String type, @Query("include_adult") boolean adult);
  
  @GET("genre/{type}/list?language=en-US&api_key=" + BuildConfig.MOVIEDB_API_KEY)
  Call<Genre.Wrapper> getGenres(@Path("type") String type);
  
  @GET("search/{type}?language=en-US&api_key=" + BuildConfig.MOVIEDB_API_KEY)
  Call<MovieWrapper> searchMovies(@Path("type") String type, @Query("query") String query);
}

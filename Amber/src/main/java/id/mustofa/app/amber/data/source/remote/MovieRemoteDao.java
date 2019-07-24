package id.mustofa.app.amber.data.source.remote;

import java.util.Map;

import id.mustofa.app.amber.BuildConfig;
import id.mustofa.app.amber.data.model.Genre;
import id.mustofa.app.amber.data.model.MovieWrapper;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

/**
 * @author Habib Mustofa
 * Indonesia on 06/07/19.
 */
public interface MovieRemoteDao {
  
  @GET("discover/{type}?language=en-US&api_key=" + BuildConfig.MOVIEDB_API_KEY)
  Call<MovieWrapper> getDiscovers(@Path("type") String type, @QueryMap Map<String, String> queryMap);
  
  @GET("genre/{type}/list?language=en-US&api_key=" + BuildConfig.MOVIEDB_API_KEY)
  Call<Genre.Wrapper> getGenres(@Path("type") String type);
  
  @GET("search/{type}?language=en-US&api_key=" + BuildConfig.MOVIEDB_API_KEY)
  Call<MovieWrapper> searchMovies(@Path("type") String type, @QueryMap Map<String, String> queryMap);
  
  @GET("trending/{type}/day?language=en-US&api_key=" + BuildConfig.MOVIEDB_API_KEY)
  Call<MovieWrapper> getTrendingMovies(@Path("type") String type, @QueryMap Map<String, String> queryMap);
  
  @GET("{type}/popular?language=en-US&api_key=" + BuildConfig.MOVIEDB_API_KEY)
  Call<MovieWrapper> getPopularMovies(@Path("type") String type, @QueryMap Map<String, String> queryMap);
  
  @GET("{type}/{id}/similar?language=en-US&api_key=" + BuildConfig.MOVIEDB_API_KEY)
  Call<MovieWrapper> getSimilarMovies(@Path("type") String type, @Path("id") long id, @QueryMap Map<String, String> queryMap);
}

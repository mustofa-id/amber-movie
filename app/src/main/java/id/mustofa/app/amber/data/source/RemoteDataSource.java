package id.mustofa.app.amber.data.source;

import id.mustofa.app.amber.BuildConfig;
import id.mustofa.app.amber.data.model.Genre;
import id.mustofa.app.amber.data.model.MovieWrapper;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * @author Habib Mustofa
 * Indonesia on 06/07/19.
 */
public interface RemoteDataSource {
  
  @GET("discover/{type}?language=en-US&api_key=" + BuildConfig.MOVIEDB_API_KEY)
  Call<MovieWrapper> getDiscovers(@Path("type") String type);
  
  @GET("genre/{type}/list?language=en-US&api_key=" + BuildConfig.MOVIEDB_API_KEY)
  Call<Genre.Wrapper> getGenres(@Path("type") String type);
}

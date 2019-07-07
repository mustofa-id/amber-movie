package id.mustofa.app.amber.data.source.remote;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author Habib Mustofa
 * Indonesia on 06/07/19.
 */
public final class ApiClient {
  
  private static final String BASE_URL = "https://api.themoviedb.org/3/";
  private static Retrofit sRetrofit;
  
  private ApiClient() {}
  
  public static Retrofit retrofit() {
    if (sRetrofit == null) {
      synchronized (ApiClient.class) {
        if (sRetrofit == null) {
          sRetrofit = new Retrofit.Builder()
              .baseUrl(BASE_URL)
              .addConverterFactory(GsonConverterFactory.create())
              .build();
        }
      }
    }
    return sRetrofit;
  }
}

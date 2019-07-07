package id.mustofa.app.amber.data.source.remote;

import org.junit.Test;

import java.io.IOException;

import id.mustofa.app.amber.BuildConfig;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.GET;

import static org.junit.Assert.*;

/**
 * @author Habib Mustofa
 * <p>
 * Typing with Love
 * Indonesia on 06/07/19.
 */
public class ApiClientTest {
  
  interface Service {
    @GET("?api_key=" + BuildConfig.MOVIEDB_API_KEY)
    Call<Void> get();
  }
  
  @Test
  public void apiResponseStatusShouldBeGt200() throws IOException {
    Service service = ApiClient.retrofit().create(Service.class);
    Response<Void> execute = service.get().execute();
    boolean successful = execute.isSuccessful();
    assertTrue(successful);
  }
}
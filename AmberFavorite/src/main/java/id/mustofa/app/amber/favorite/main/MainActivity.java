package id.mustofa.app.amber.favorite.main;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.List;

import id.mustofa.app.amber.favorite.R;
import id.mustofa.app.amber.favorite.data.DataAccessAsync;
import id.mustofa.app.amber.favorite.data.model.Movie;

public class MainActivity extends AppCompatActivity implements DataAccessAsync.Callback<List<Movie>> {
  
  private static final String TAG = MainActivity.class.getName();
  
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    new DataAccessAsync.GetMovies(getContentResolver(), this).execute();
  }
  
  @Override
  public void onResult(List<Movie> movies) {
    for (Movie m : movies) {
      Log.d(TAG, "onResult: " + m);
    }
  }
}

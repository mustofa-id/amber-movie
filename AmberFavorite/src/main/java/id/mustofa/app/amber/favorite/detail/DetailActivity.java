package id.mustofa.app.amber.favorite.detail;

import android.content.ContentResolver;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import id.mustofa.app.amber.favorite.R;
import id.mustofa.app.amber.favorite.data.DataAccessAsync;
import id.mustofa.app.amber.favorite.data.model.Movie;

public class DetailActivity extends AppCompatActivity {
  
  private static final String TAG = DetailActivity.class.getName();
  
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_detail);
    getMovieData();
  }
  
  private void getMovieData() {
    Uri uri = getIntent().getData();
    if (uri == null) {
      Toast.makeText(this, "Can't load movie data!", Toast.LENGTH_SHORT).show();
      return;
    }
    final ContentResolver resolver = getContentResolver();
    new DataAccessAsync.GetMovie(resolver, this::onGetMovieResult).execute(uri);
  }
  
  public void onGetMovieResult(Movie movie) {
    Log.d(TAG, "onGetMovieResult: " + movie);
  }
}

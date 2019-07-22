package id.mustofa.app.amber.favorite.detail;

import android.content.ContentResolver;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import id.mustofa.app.amber.favorite.R;
import id.mustofa.app.amber.favorite.data.DataAccessAsync;
import id.mustofa.app.amber.favorite.data.model.Movie;
import id.mustofa.app.amber.favorite.util.ImageLoader;

public class DetailActivity extends AppCompatActivity {
  
  private static final String TAG = DetailActivity.class.getName();
  
  private ImageView mPoster, mBackdrop;
  private TextView mTitle;
  
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_detail);
    getMovieData();
  
    mPoster = findViewById(R.id.img_detail_poster);
    mBackdrop = findViewById(R.id.img_detail_backdrop);
    mTitle = findViewById(R.id.tv_detail_title);
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
    ImageLoader.load(this, movie.getPosterPath(), mPoster);
    ImageLoader.load(this, movie.getBackdropPath(), mBackdrop, "w500");
  
    mTitle.setText(movie.getTitle());
  }
  
  public void onRemoveFavorite(View view) {
  
  }
  
  public void onPlayTrailer(View view) {
  
  }
}

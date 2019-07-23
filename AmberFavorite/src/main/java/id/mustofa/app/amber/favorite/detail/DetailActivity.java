package id.mustofa.app.amber.favorite.detail;

import android.content.ContentResolver;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Objects;

import id.mustofa.app.amber.favorite.R;
import id.mustofa.app.amber.favorite.data.DataAccessAsync;
import id.mustofa.app.amber.favorite.data.model.Movie;
import id.mustofa.app.amber.favorite.util.ImageLoader;

public class DetailActivity extends AppCompatActivity {
  
  private ImageView mPoster, mBackdrop;
  private TextView mTitle, mOverview, mDetail;
  private RatingBar mRating;
  
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_detail);
  
    Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
  
    bindViews();
    getMovieData();
  }
  
  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    if (item.getItemId() == android.R.id.home) {
      onBackPressed();
    }
    return super.onOptionsItemSelected(item);
  }
  
  private void bindViews() {
    mPoster = findViewById(R.id.img_detail_poster);
    mBackdrop = findViewById(R.id.img_detail_backdrop);
    mTitle = findViewById(R.id.text_detail_movie_title);
    mOverview = findViewById(R.id.text_detail_movie_overview);
    mDetail = findViewById(R.id.text_detail_movie_details);
    mRating = findViewById(R.id.rt_detail_rating);
  }
  
  private void getMovieData() {
    Uri uri = getIntent().getData();
    if (uri == null) {
      Toast.makeText(this, R.string.msg_load_movie_fail, Toast.LENGTH_SHORT).show();
      return;
    }
    final ContentResolver resolver = getContentResolver();
    new DataAccessAsync
        .GetMovie(resolver, this::onGetMovieResult)
        .execute(uri);
  }
  
  public void onGetMovieResult(Movie movie) {
    setMovieToViews(movie);
  }
  
  private void setMovieToViews(Movie movie) {
    ImageLoader.load(this, movie.getPosterPath(), mPoster);
    ImageLoader.load(this, movie.getBackdropPath(), mBackdrop, "w500");
    
    mTitle.setText(movie.getTitle());
    mRating.setRating(movie.getVoteAverage() / 2);
    mOverview.setText(movie.getOverview());
    
    String hOriginalTitle = getString(R.string.title_original_title);
    String hReleaseDate = getString(R.string.title_release_date);
    String hPopularity = getString(R.string.title_popularity);
    String hVoteCount = getString(R.string.title_vote_count);
    String hVoteAverage = getString(R.string.title_vote_average);
    String hOriginLang = getString(R.string.title_original_lang);
    
    StringBuilder string = new StringBuilder();
    string.append(hOriginalTitle).append(movie.getOriginalTitle()).append('\n');
    string.append(hReleaseDate).append(movie.getReleaseDate()).append('\n');
    string.append(hPopularity).append(movie.getPopularity()).append('\n');
    string.append(hVoteCount).append(movie.getVoteCount()).append('\n');
    string.append(hVoteAverage).append(movie.getVoteAverage()).append('\n');
    string.append(hOriginLang).append(movie.getOriginalLanguage());
    mDetail.setText(string);
  }
  
  public void onRemoveFavorite(View view) {
    final ContentResolver resolver = getContentResolver();
    final Uri data = getIntent().getData();
    new DataAccessAsync
        .RemoveMovie(resolver, this::onRemoveFavoriteResult)
        .execute(data);
  }
  
  private void onRemoveFavoriteResult(int count) {
    int message = count > 0 ? R.string.msg_remove_ok : R.string.mgs_remove_fail;
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
  }
  
  public void onPlayTrailer(View view) {
    Toast.makeText(this, R.string.msg_playing_trailer, Toast.LENGTH_SHORT).show();
  }
}

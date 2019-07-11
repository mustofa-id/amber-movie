package id.mustofa.app.amber.moviedetail;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.design.chip.Chip;
import android.support.design.chip.ChipGroup;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.Objects;

import id.mustofa.app.amber.R;
import id.mustofa.app.amber.base.BaseAppCompatActivity;
import id.mustofa.app.amber.data.model.Genre;
import id.mustofa.app.amber.data.model.MediaType;
import id.mustofa.app.amber.data.model.Movie;
import id.mustofa.app.amber.util.DateUtil;
import id.mustofa.app.amber.util.ImageLoader;
import id.mustofa.app.amber.util.ViewModelFactory;

/**
 * @author Habib Mustofa
 * Indonesia on 06/07/19.
 */
public class MovieDetailActivity extends BaseAppCompatActivity {
  
  public static final String EXTRA_MOVIE_ITEM = "MOVIE_ITEM__";
  public static final String EXTRA_MOVIE_TYPE = "MOVIE_TYPE__";
  
  private MovieDetailViewModel mMovieDetailViewModel;
  
  private FloatingActionButton mFabPlayTrailer;
  private ImageView mImagePoster, mImageBackdrop;
  private TextView mTextTitle, mTextDate, mTextOverview;
  private TextView mTextOriginalTitle, mTextPopularity, mTextVoteCount, mTextOriginalLang;
  private TextView mTextGenreMessage;
  private RatingBar mRateRating;
  private ChipGroup mChipGroupGenre;
  
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_movie_detail);
    setupToolbar();
    setupViewModel();
    bindViews();
    populateMovieToViews();
    subscribeViewModelChange();
    mFabPlayTrailer.setOnClickListener(this::onPlayTrailer);
  }
  
  private void setupViewModel() {
    ViewModelFactory factory = ViewModelFactory.getInstance(getApplication());
    mMovieDetailViewModel = ViewModelProviders.of(this, factory).get(MovieDetailViewModel.class);
  }
  
  private void subscribeViewModelChange() {
    mMovieDetailViewModel.getGenres().observe(this, this::onGenresLoaded);
    mMovieDetailViewModel.getMessageResId().observe(this, this::onGenresHasMessage);
  }
  
  private void setupToolbar() {
    Toolbar toolbar = findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
  }
  
  private void bindViews() {
    mFabPlayTrailer = findViewById(R.id.fab_movie_detail_play_trailer);
    mImagePoster = findViewById(R.id.img_movie_detail_poster);
    mImageBackdrop = findViewById(R.id.img_movie_detail_poster_backdrop);
    mTextTitle = findViewById(R.id.text_movie_detail_title);
    mTextDate = findViewById(R.id.text_movie_detail_date);
    mTextOverview = findViewById(R.id.text_movie_detail_overview);
    mTextPopularity = findViewById(R.id.text_movie_detail_popularity);
    mTextVoteCount = findViewById(R.id.text_movie_detail_vote_count);
    mTextOriginalTitle = findViewById(R.id.text_movie_detail_original_title);
    mTextOriginalLang = findViewById(R.id.text_movie_detail_original_lang);
    mTextGenreMessage = findViewById(R.id.text_movie_detail_genres_message);
    mRateRating = findViewById(R.id.rate_movie_detail_rating);
    mChipGroupGenre = findViewById(R.id.cg_movie_detail_chips);
  }
  
  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    if (item.getItemId() == android.R.id.home) onBackPressed();
    return super.onOptionsItemSelected(item);
  }
  
  private void populateMovieToViews() {
    Movie movie = getIntent().getParcelableExtra(EXTRA_MOVIE_ITEM);
    MediaType mediaType = (MediaType) getIntent().getSerializableExtra(EXTRA_MOVIE_TYPE);
    if (movie == null || mediaType == null) {
      Toast.makeText(this, R.string.error_unknown, Toast.LENGTH_SHORT).show();
      return;
    }
    mMovieDetailViewModel.setMediaType(mediaType);
    mMovieDetailViewModel.setGenreIds(movie.getGenreIds());
    ImageLoader.load(this, movie.getPosterPath(), mImagePoster);
    ImageLoader.load(this, movie.getBackdropPath(), mImageBackdrop);
    setTitle(movie.getTitle());
    mTextTitle.setText(movie.getTitle());
    mTextDate.setText(DateUtil.reformatDate(movie.getReleaseDate(), "dd MMMM yyyy"));
    mTextPopularity.setText(String.valueOf(movie.getPopularity()));
    mTextVoteCount.setText(String.valueOf(movie.getVoteCount()));
    mTextOriginalTitle.setText(movie.getOriginalTitle());
    mTextOriginalLang.setText(movie.getOriginalLanguage());
    mRateRating.setRating(movie.getVoteAverage() / 2);
    mTextOverview.setText(movie.getOverview());
  }
  
  private void onGenresLoaded(List<Genre> genres) {
    for (Genre genre : genres) {
      Chip chip = new Chip(this);
      chip.setText(genre.getName());
      chip.setClickable(false);
      mChipGroupGenre.addView(chip);
    }
  }
  
  private void onGenresHasMessage(int message) {
    mTextGenreMessage.setVisibility(message == 0 ? View.GONE : View.VISIBLE);
    if (message != 0) mTextGenreMessage.setText(message);
  }
  
  @SuppressWarnings("unused")
  private void onPlayTrailer(View view) {
    Toast.makeText(this, R.string.msg_playing_trailer, Toast.LENGTH_SHORT).show();
  }
}

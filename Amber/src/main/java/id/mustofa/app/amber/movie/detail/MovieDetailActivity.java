package id.mustofa.app.amber.movie.detail;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.chip.Chip;
import android.support.design.chip.ChipGroup;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
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
import id.mustofa.app.amber.movie.all.MovieAllGridAdapter;
import id.mustofa.app.amber.movie.all.MovieAllItemNavigator;
import id.mustofa.app.amber.util.DateUtil;
import id.mustofa.app.amber.util.ImageLoader;
import id.mustofa.app.amber.util.ViewModelFactory;
import id.mustofa.app.amber.widget.MovieFavoriteWidget;

/**
 * @author Habib Mustofa
 * Indonesia on 06/07/19.
 */
public class MovieDetailActivity extends BaseAppCompatActivity implements MovieAllItemNavigator {
  
  public static final String EXTRA_MOVIE_ITEM = "MOVIE_ITEM__";
  public static final String EXTRA_MOVIE_TYPE = "MOVIE_TYPE__";
  
  private MovieDetailViewModel mMovieDetailViewModel;
  private SharedPreferences mPrefs;
  
  private FloatingActionButton mFabPlayTrailer;
  private ImageView mImagePoster, mImageBackdrop;
  private TextView mTextTitle, mTextDate, mTextOverview;
  private TextView mTextOriginalTitle, mTextPopularity, mTextVoteCount, mTextOriginalLang;
  private TextView mTextGenreMessage;
  private RatingBar mRateRating;
  private ChipGroup mChipGroupGenre;
  private MovieAllGridAdapter mGridAdapter;
  private MenuItem mMenuFavorite, mMenuRemoveFavorite;
  
  private Movie mMovie;
  private MediaType mMediaType;
  
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_movie_detail);
    mPrefs = PreferenceManager.getDefaultSharedPreferences(this);
  
    getExtras();
    setupToolbar();
    setupViewModel();
    bindViews();
    populateMovieToViews();
    setupRecyclerView();
    subscribeViewModelChange();
    mFabPlayTrailer.setOnClickListener(this::onPlayTrailer);
  }
  
  @Override
  protected void onResume() {
    super.onResume();
    boolean includeAdult = !mPrefs.getBoolean(getString(R.string.key_prefs_restricted_mode), false);
    mMovieDetailViewModel.loadSimilar(mMovie.getId(), includeAdult);
  }
  
  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.menu_activity_movie_detail, menu);
    mMenuFavorite = menu.findItem(R.id.act_movie_detail_favorite);
    mMenuRemoveFavorite = menu.findItem(R.id.act_movie_detail_remove_favorite);
    mMovieDetailViewModel.checkFavorite(mMovie.getId());
    return super.onCreateOptionsMenu(menu);
  }
  
  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    int id = item.getItemId();
    if (id == android.R.id.home) {
      onBackPressed();
    } else if (id == mMenuFavorite.getItemId()) {
      mMovieDetailViewModel.addToFavorite(mMovie);
      updateMovieFavoriteWidget();
    } else if (id == mMenuRemoveFavorite.getItemId()) {
      mMovieDetailViewModel.removeFromFavorite(mMovie);
      updateMovieFavoriteWidget();
    }
    return super.onOptionsItemSelected(item);
  }
  
  private void setupViewModel() {
    ViewModelFactory factory = ViewModelFactory.getInstance(getApplication());
    mMovieDetailViewModel = ViewModelProviders.of(this, factory).get(MovieDetailViewModel.class);
  }
  
  private void setupRecyclerView() {
    mGridAdapter = new MovieAllGridAdapter(mMediaType);
    mGridAdapter.setItemNavigator(this);
    RecyclerView recyclerView = findViewById(R.id.rv_detail_similar_movies);
    recyclerView.setHasFixedSize(true);
    recyclerView.setLayoutManager(new LinearLayoutManager(
        this, LinearLayoutManager.HORIZONTAL, false));
    recyclerView.setAdapter(mGridAdapter);
  }
  
  private void subscribeViewModelChange() {
    mMovieDetailViewModel.getGenres().observe(this, this::onGenresLoaded);
    mMovieDetailViewModel.getGenresMessageResId().observe(this, this::onGenresHasMessage);
    mMovieDetailViewModel.getIsFavorite().observe(this, this::onIsFavorite);
    mMovieDetailViewModel.getFavoriteMessageResId().observe(this, this::onFavoriteHasMessage);
    mMovieDetailViewModel.getSimilarMovie().observe(this, mGridAdapter::populateMovies);
    mMovieDetailViewModel.getSimilarMovieMessageResId().observe(this, this::onSimilarMovieHasMessage);
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
  
  private void getExtras() {
    mMovie = getIntent().getParcelableExtra(EXTRA_MOVIE_ITEM);
    mMediaType = (MediaType) getIntent().getSerializableExtra(EXTRA_MOVIE_TYPE);
  }
  
  private void populateMovieToViews() {
    if (mMovie == null || mMediaType == null) {
      Toast.makeText(this, R.string.error_unknown, Toast.LENGTH_SHORT).show();
      return;
    }
    mMovieDetailViewModel.setMediaType(mMediaType);
    mMovieDetailViewModel.setGenreIds(mMovie.getGenreIds());
    ImageLoader.load(this, mMovie.getPosterPath(), mImagePoster);
    ImageLoader.load(this, mMovie.getBackdropPath(), mImageBackdrop);
    setTitle(mMovie.getTitle());
    mTextTitle.setText(mMovie.getTitle());
    mTextDate.setText(DateUtil.reformatDate(mMovie.getReleaseDate(), "dd MMMM yyyy"));
    mTextPopularity.setText(String.valueOf(mMovie.getPopularity()));
    mTextVoteCount.setText(String.valueOf(mMovie.getVoteCount()));
    mTextOriginalTitle.setText(mMovie.getOriginalTitle());
    mTextOriginalLang.setText(mMovie.getOriginalLanguage());
    mRateRating.setRating(mMovie.getVoteAverage() / 2);
    mTextOverview.setText(mMovie.getOverview());
  }
  
  private void updateMovieFavoriteWidget() {
    MovieFavoriteWidget.notifyDataChanged(this);
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
  
  private void onIsFavorite(Boolean isFavorite) {
    mMenuFavorite.setVisible(isFavorite);
    mMenuRemoveFavorite.setVisible(!isFavorite);
  }
  
  private void onFavoriteHasMessage(int message) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
  }
  
  private void onSimilarMovieHasMessage(int resId) {
    Toast.makeText(this, resId, Toast.LENGTH_SHORT).show();
  }
  
  @SuppressWarnings("unused")
  private void onPlayTrailer(View view) {
    Toast.makeText(this, R.string.msg_playing_trailer, Toast.LENGTH_SHORT).show();
  }
  
  @Override
  public void openMovieDetail(Movie movie, MediaType type) {
    Intent movieDetailIntent = new Intent(this, MovieDetailActivity.class);
    movieDetailIntent.putExtra(MovieDetailActivity.EXTRA_MOVIE_ITEM, movie);
    movieDetailIntent.putExtra(MovieDetailActivity.EXTRA_MOVIE_TYPE, type);
    startActivity(movieDetailIntent);
  }
}

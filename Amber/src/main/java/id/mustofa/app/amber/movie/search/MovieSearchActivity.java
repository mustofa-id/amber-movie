package id.mustofa.app.amber.movie.search;

import android.app.SearchManager;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

import id.mustofa.app.amber.R;
import id.mustofa.app.amber.base.BaseAppCompatActivity;
import id.mustofa.app.amber.data.model.MediaType;
import id.mustofa.app.amber.data.model.Movie;
import id.mustofa.app.amber.movie.MovieListAdapter;
import id.mustofa.app.amber.movie.detail.MovieDetailActivity;
import id.mustofa.app.amber.util.InfiniteScroll;
import id.mustofa.app.amber.util.ViewModelFactory;

public class MovieSearchActivity extends BaseAppCompatActivity implements SearchView.OnQueryTextListener {
  
  private Map<MovieSearchType, Integer> mQueryHints;
  private MovieSearchViewModel mSearchViewModel;
  private SharedPreferences mPrefs;
  private MovieListAdapter mResultAdapter;
  
  private SearchView mSearchView;
  private ProgressBar mLoading;
  private TextView mTextMessage;
  
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_movie_search);
    mLoading = findViewById(R.id.pb_search_loading);
    mTextMessage = findViewById(R.id.text_search_message);
    setupPrefs();
    setupViewModel();
    setupResultAdapter();
    setupToolbar();
    setupSearchView();
    setupRecyclerView();
    setupQueryHints();
    subscribeViewModelChange();
  }
  
  @Override
  public void onBackPressed() {
    boolean queryEmpty = TextUtils.isEmpty(mSearchView.getQuery());
    if (queryEmpty || mSearchView.isIconified()) {
      super.onBackPressed();
    }
    mSearchView.setQuery(null, false);
    mSearchView.setIconified(true);
  }
  
  private void setupPrefs() {
    mPrefs = PreferenceManager.getDefaultSharedPreferences(this);
  }
  
  private void setupViewModel() {
    final ViewModelFactory factory = ViewModelFactory.getInstance(this);
    mSearchViewModel = ViewModelProviders.of(this, factory).get(MovieSearchViewModel.class);
    boolean includeAdult = !mPrefs.getBoolean(getString(R.string.key_prefs_restricted_mode), false);
    mSearchViewModel.setIncludeAdult(includeAdult);
  }
  
  private void setupResultAdapter() {
    mResultAdapter = new MovieListAdapter();
    mResultAdapter.setMovieItemNavigator(this::openMovieDetail);
  }
  
  private void setupToolbar() {
    Toolbar toolbar = findViewById(R.id.toolbar_search);
    toolbar.setOverflowIcon(ContextCompat.getDrawable(this, R.drawable.ic_filter_list_light_24dp));
    setSupportActionBar(toolbar);
    ActionBar actionBar = getSupportActionBar();
    if (actionBar != null) actionBar.setDisplayHomeAsUpEnabled(true);
  }
  
  private void setupQueryHints() {
    mQueryHints = new HashMap<>();
    mQueryHints.put(MovieSearchType.MOVIE, R.string.action_search_hint_movie);
    mQueryHints.put(MovieSearchType.TV, R.string.action_search_hint_tv);
    mQueryHints.put(MovieSearchType.FAVORITE, R.string.action_search_hint_favorite);
  }
  
  private void setupSearchView() {
    final SearchManager searchManager = (SearchManager) this.getSystemService(Context.SEARCH_SERVICE);
    mSearchView = findViewById(R.id.sv_search_input);
    if (searchManager != null) {
      mSearchView.setSearchableInfo(searchManager.getSearchableInfo(this.getComponentName()));
    }
    mSearchView.setOnQueryTextListener(this);
    mSearchView.setOnCloseListener(() -> true);
    mSearchView.setQueryHint(getString(R.string.action_search_hint_movie));
    mSearchView.requestFocus();
  }
  
  private void setupRecyclerView() {
    RecyclerView recyclerView = findViewById(R.id.rv_search_result);
    recyclerView.setHasFixedSize(true);
    LinearLayoutManager layoutManager = new LinearLayoutManager(this);
    recyclerView.setLayoutManager(layoutManager);
    recyclerView.setAdapter(mResultAdapter);
    InfiniteScroll.with(recyclerView).and(layoutManager)
        .listen(() -> Toast.makeText(this, "LOAD_MORE", Toast.LENGTH_SHORT).show());
  }
  
  private void subscribeViewModelChange() {
    mSearchViewModel.getMovieResult().observe(this, mResultAdapter::populateMovies);
    mSearchViewModel.getSearchType().observe(this, this::onSearchTypeChanged);
    mSearchViewModel.getLoading().observe(this, this::onLoading);
    mSearchViewModel.getMessageResId().observe(this, this::onHasMessage);
  }
  
  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.menu_activity_search, menu);
    return super.onCreateOptionsMenu(menu);
  }
  
  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    int id = item.getItemId();
    if (id == android.R.id.home) {
      onBackPressed();
    } else if (id == R.id.act_search_filter_movie) {
      mSearchViewModel.setSearchType(MovieSearchType.MOVIE);
    } else if (id == R.id.act_search_filter_tv) {
      mSearchViewModel.setSearchType(MovieSearchType.TV);
    } else if (id == R.id.act_search_filter_favorite) {
      mSearchViewModel.setSearchType(MovieSearchType.FAVORITE);
    }
    return super.onOptionsItemSelected(item);
  }
  
  @Override
  public boolean onQueryTextSubmit(String query) {
    mSearchViewModel.search(query);
    return true;
  }
  
  @Override
  public boolean onQueryTextChange(String s) {
    return false;
  }
  
  private void onLoading(Boolean loading) {
    mLoading.setVisibility(loading ? View.VISIBLE : View.GONE);
  }
  
  private void onHasMessage(int resId) {
    mTextMessage.setVisibility(resId == 0 ? View.GONE : View.VISIBLE);
    if (resId != 0) mTextMessage.setText(resId);
  }
  
  private void openMovieDetail(Movie movie) {
    Intent movieDetailIntent = new Intent(this, MovieDetailActivity.class);
    movieDetailIntent.putExtra(MovieDetailActivity.EXTRA_MOVIE_ITEM, movie);
    movieDetailIntent.putExtra(MovieDetailActivity.EXTRA_MOVIE_TYPE, getMediaType(movie.getType()));
    startActivity(movieDetailIntent);
  }
  
  @SuppressWarnings("ConstantConditions")
  private void onSearchTypeChanged(MovieSearchType type) {
    int queryHint = mQueryHints.get(type);
    mSearchView.setQueryHint(getString(queryHint));
    CharSequence query = mSearchView.getQuery();
    if (query != null && !TextUtils.isEmpty(query)) {
      mSearchView.setQuery(query, true);
    }
  }
  
  private MediaType getMediaType(String typeMovie) {
    final MovieSearchType type = mSearchViewModel.getSearchType().getValue();
    if (typeMovie != null) {
      return typeMovie.equals(MediaType.MOVIE.getValue()) ? MediaType.MOVIE : MediaType.TV;
    } else if (type == MovieSearchType.MOVIE) {
      return MediaType.MOVIE;
    } else if (type == MovieSearchType.TV) {
      return MediaType.TV;
    }
    return null;
  }
}

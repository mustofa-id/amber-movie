package id.mustofa.app.amber.movie;


import android.app.SearchManager;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Objects;

import id.mustofa.app.amber.R;
import id.mustofa.app.amber.data.model.MediaType;
import id.mustofa.app.amber.data.model.Movie;
import id.mustofa.app.amber.moviedetail.MovieDetailActivity;
import id.mustofa.app.amber.util.ViewModelFactory;

/**
 * @author Habib Mustofa
 * Indonesia on 06/07/19.
 */
public class MovieFragment extends Fragment implements MovieItemNavigator {
  
  private static final String ARGS_MEDIA_TYPE = "MEDIA_TYPE__";
  private SharedPreferences mPrefs;
  private MovieListAdapter mMovieListAdapter;
  private MovieViewModel mMovieViewModel;
  
  private SwipeRefreshLayout mSrRefresher;
  private TextView mTextInfo;
  private SearchView mSearchView;
  
  public static MovieFragment newInstance(MediaType type) {
    MovieFragment movieFragment = new MovieFragment();
    Bundle bundle = new Bundle();
    bundle.putSerializable(ARGS_MEDIA_TYPE, type);
    movieFragment.setArguments(bundle);
    return movieFragment;
  }
  
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_movie, container, false);
  }
  
  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    setHasOptionsMenu(true);
    setupPrefs();
    setupViewModel();
    setupRecyclerViewAdapter();
    setupRefresher(view);
    setupInfo(view);
    setupRecyclerView(view);
    subscribeViewModelChange();
  }
  
  @Override
  public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    inflater.inflate(R.menu.menu_fragment_movie, menu);
    setupSearchView(menu);
    super.onCreateOptionsMenu(menu, inflater);
  }
  
  private void setupSearchView(Menu menu) {
    if (getActivity() == null) return;
    final SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
    if (searchManager != null) {
      MenuItem searchItem = menu.findItem(R.id.act_main_search);
      mSearchView = (SearchView) searchItem.getActionView();
      mSearchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
      mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
        
        // for prevent default action
        private boolean tick = false;
        
        @Override
        public boolean onQueryTextSubmit(String query) {
          mMovieViewModel.searchMovies(query);
          return true;
        }
        
        @Override
        public boolean onQueryTextChange(String newText) {
          if (newText.isEmpty() && tick) mMovieViewModel.refresh();
          tick = true;
          return true;
        }
      });
  
      mSearchView.setOnCloseListener(() -> {
        mMovieViewModel.refresh();
        return true;
      });
      
      int hint = getMediaType() == MediaType.MOVIE ?
          R.string.action_search_hint_movie : R.string.action_search_hint_tv;
      mSearchView.setQueryHint(getString(hint));
    }
  }
  
  private void setupPrefs() {
    final Context context = Objects.requireNonNull(getContext());
    mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
  }
  
  private void setupViewModel() {
    ViewModelFactory factory = ViewModelFactory.getInstance(getContext());
    mMovieViewModel = ViewModelProviders.of(this, factory).get(MovieViewModel.class);
    mMovieViewModel.setMediaType(getMediaType());
    boolean includeAdult = !mPrefs.getBoolean(getString(R.string.key_prefs_restricted_mode), false);
    mMovieViewModel.setIncludeAdult(includeAdult);
    mMovieViewModel.start();
  }
  
  private void setupInfo(@NonNull View view) {
    mTextInfo = view.findViewById(R.id.er_fragment_movie);
    mTextInfo.setVisibility(View.GONE);
  }
  
  private void setupRefresher(@NonNull View view) {
    mSrRefresher = view.findViewById(R.id.sr_fragment_movie);
    mSrRefresher.setOnRefreshListener(() -> {
      mMovieViewModel.refresh();
      mSearchView.setIconified(true);
      mSearchView.clearFocus();
    });
  }
  
  private void setupRecyclerViewAdapter() {
    mMovieListAdapter = new MovieListAdapter(getViewLifecycleOwner(), mMovieViewModel);
    mMovieListAdapter.setMovieItemNavigator(this);
  }
  
  private void setupRecyclerView(@NonNull View view) {
    RecyclerView recyclerView = view.findViewById(R.id.rv_fragment_movie);
    recyclerView.setHasFixedSize(true);
    recyclerView.setItemViewCacheSize(20);
    // Using grid layout with 2 cols span on landscape
    int orientation = getResources().getConfiguration().orientation;
    if (orientation == Configuration.ORIENTATION_PORTRAIT) {
      recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    } else if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
      recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
    }
    recyclerView.setAdapter(mMovieListAdapter);
  }
  
  private void subscribeViewModelChange() {
    final LifecycleOwner lifecycleOwner = getViewLifecycleOwner();
    mMovieViewModel.getLoading().observe(lifecycleOwner, mSrRefresher::setRefreshing);
    mMovieViewModel.getMessageResId().observe(lifecycleOwner, this::onHasMessage);
  }
  
  private void onHasMessage(int message) {
    mTextInfo.setVisibility(message == 0 ? View.GONE : View.VISIBLE);
    if (message != 0) {
      mTextInfo.setText(message);
      Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }
  }
  
  @Override
  public void openMovieDetail(Movie movie) {
    Intent movieDetailIntent = new Intent(getActivity(), MovieDetailActivity.class);
    movieDetailIntent.putExtra(MovieDetailActivity.EXTRA_MOVIE_ITEM, movie);
    movieDetailIntent.putExtra(MovieDetailActivity.EXTRA_MOVIE_TYPE, getMediaType());
    startActivity(movieDetailIntent);
  }
  
  private MediaType getMediaType() {
    assert getArguments() != null;
    return (MediaType) getArguments().getSerializable(ARGS_MEDIA_TYPE);
  }
}

package id.mustofa.app.amber.movie.all;


import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.Objects;

import id.mustofa.app.amber.R;
import id.mustofa.app.amber.data.model.MediaType;
import id.mustofa.app.amber.data.model.Movie;
import id.mustofa.app.amber.movie.detail.MovieDetailActivity;
import id.mustofa.app.amber.util.ViewModelFactory;

public class MovieAllFragment extends Fragment implements MovieAllItemNavigator {
  
  private MovieAllSectionAdapter mSectionAdapter;
  private MovieAllViewModel mMovieAllViewModel;
  private SharedPreferences mPrefs;
  
  private MovieAllSectionItem mMovieTrendingItem;
  private MovieAllSectionItem mTvTrendingItem;
  private MovieAllSectionItem mMoviePoplarItem;
  private MovieAllSectionItem mTvPoplarItem;
  private MovieAllSectionItem mMovieReleaseTodayItem;
  
  public static MovieAllFragment newInstance() {
    return new MovieAllFragment();
  }
  
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_movie_all, container, false);
  }
  
  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    setupPrefs();
    setupAdapter();
    setupViewModel();
    setupRecyclerView(view);
  }
  
  private void setupPrefs() {
    final Context context = Objects.requireNonNull(getContext());
    mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
  }
  
  private void setupAdapter() {
    mSectionAdapter = new MovieAllSectionAdapter(this);
    
    mMovieReleaseTodayItem = new MovieAllSectionItem(getString(R.string.title_release_today_movie), MediaType.MOVIE);
    mMovieTrendingItem = new MovieAllSectionItem(getString(R.string.title_trending_movie), MediaType.MOVIE);
    mTvTrendingItem = new MovieAllSectionItem(getString(R.string.title_trending_tv), MediaType.TV);
    mMoviePoplarItem = new MovieAllSectionItem(getString(R.string.title_popular_movie), MediaType.MOVIE);
    mTvPoplarItem = new MovieAllSectionItem(getString(R.string.title_popular_tv), MediaType.TV);
    
    mSectionAdapter.addSectionItem(mMovieReleaseTodayItem);
    mSectionAdapter.addSectionItem(mMovieTrendingItem);
    mSectionAdapter.addSectionItem(mTvTrendingItem);
    mSectionAdapter.addSectionItem(mMoviePoplarItem);
    mSectionAdapter.addSectionItem(mTvPoplarItem);
    
    mSectionAdapter.notifyDataSetChanged();
  }
  
  private void setupViewModel() {
    final LifecycleOwner lifecycleOwner = getViewLifecycleOwner();
    final ViewModelFactory factory = ViewModelFactory.getInstance(getContext());
    mMovieAllViewModel = ViewModelProviders.of(this, factory).get(MovieAllViewModel.class);
    
    boolean includeAdult = !mPrefs.getBoolean(getString(R.string.key_prefs_restricted_mode), false);
    mMovieAllViewModel.setIncludeAdult(includeAdult);
    
    mMovieAllViewModel.getMovieTrending().observe(lifecycleOwner, mMovieTrendingItem::populateMovies);
    mMovieAllViewModel.getTvTrending().observe(lifecycleOwner, mTvTrendingItem::populateMovies);
    mMovieAllViewModel.getMoviePopular().observe(lifecycleOwner, mMoviePoplarItem::populateMovies);
    mMovieAllViewModel.getTvPopular().observe(lifecycleOwner, mTvPoplarItem::populateMovies);
    mMovieAllViewModel.getMovieReleaseToday().observe(lifecycleOwner, mMovieReleaseTodayItem::populateMovies);
    mMovieAllViewModel.getMessageResId().observe(lifecycleOwner, this::onHasMessage);
  }
  
  private void setupRecyclerView(View view) {
    RecyclerView recyclerView = view.findViewById(R.id.rv_fragment_movie_all);
    recyclerView.setHasFixedSize(true);
    recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    recyclerView.setAdapter(mSectionAdapter);
  }
  
  @Override
  public void openMovieDetail(Movie movie, MediaType type) {
    Intent movieDetailIntent = new Intent(getActivity(), MovieDetailActivity.class);
    movieDetailIntent.putExtra(MovieDetailActivity.EXTRA_MOVIE_ITEM, movie);
    movieDetailIntent.putExtra(MovieDetailActivity.EXTRA_MOVIE_TYPE, type);
    startActivity(movieDetailIntent);
  }
  
  @Override
  public void openSectionDetail(MovieAllSectionItem item) {
    Toast.makeText(getContext(), "Open more: " + item.getTitle(), Toast.LENGTH_SHORT).show();
  }
  
  private void onHasMessage(int resId) {
  
  }
  
  @Override
  public void onResume() {
    super.onResume();
    mMovieAllViewModel.loadAll();
  }
}

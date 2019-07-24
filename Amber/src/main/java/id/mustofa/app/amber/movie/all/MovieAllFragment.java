package id.mustofa.app.amber.movie.all;


import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.List;

import id.mustofa.app.amber.R;
import id.mustofa.app.amber.data.model.MediaType;
import id.mustofa.app.amber.data.model.Movie;
import id.mustofa.app.amber.movie.detail.MovieDetailActivity;
import id.mustofa.app.amber.util.ViewModelFactory;

public class MovieAllFragment extends Fragment implements MovieAllItemNavigator {
  
  private MovieAllSectionAdapter mSectionAdapter;
  private MovieAllViewModel mMovieAllViewModel;
  
  private MovieAllSectionItem mMovieTrendingItem;
  
  public MovieAllFragment() {}
  
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
    
    ViewModelFactory factory = ViewModelFactory.getInstance(getContext());
    mMovieAllViewModel = ViewModelProviders.of(this, factory).get(MovieAllViewModel.class);
    
    setupAdapter();
    setupRecyclerView(view);
  }
  
  private void setupRecyclerView(View view) {
    
    RecyclerView recyclerView = view.findViewById(R.id.rv_fragment_movie_all);
    recyclerView.setHasFixedSize(true);
    recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    recyclerView.setAdapter(mSectionAdapter);
    
    mMovieAllViewModel.getMovieTrending().observe(getViewLifecycleOwner(), this::onMovieTrendingLoaded);
  }
  
  private void setupAdapter() {
    mSectionAdapter = new MovieAllSectionAdapter(this);
    
    mMovieTrendingItem = new MovieAllSectionItem("Trending", MediaType.MOVIE);
    mSectionAdapter.addSectionItem(mMovieTrendingItem);
    
    mSectionAdapter.notifyDataSetChanged();
  }
  
  private void onMovieTrendingLoaded(List<Movie> movies) {
    mMovieTrendingItem.populateMovies(movies);
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
    Toast.makeText(getContext(), item.getTitle() + ": " + item.getType(), Toast.LENGTH_SHORT).show();
  }
  
  @Override
  public void onResume() {
    super.onResume();
    mMovieAllViewModel.loadTrending();
  }
}

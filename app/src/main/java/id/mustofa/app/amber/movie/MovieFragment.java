package id.mustofa.app.amber.movie;


import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

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
  private MovieListAdapter mMovieListAdapter;
  private MovieViewModel mMovieViewModel;
  
  private SwipeRefreshLayout mSrRefresher;
  private TextView mTextInfo;
  
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
    setupViewModel();
    setupRecyclerViewAdapter();
    setupRefresher(view);
    setupInfo(view);
    setupRecyclerView(view);
    subscribeViewModelChange();
  }
  
  private void setupViewModel() {
    ViewModelFactory factory = ViewModelFactory.getInstance();
    mMovieViewModel = ViewModelProviders.of(this, factory).get(MovieViewModel.class);
    mMovieViewModel.setMediaType(getMediaType());
    mMovieViewModel.start();
  }
  
  private void setupInfo(@NonNull View view) {
    mTextInfo = view.findViewById(R.id.er_fragment_movie);
    mTextInfo.setVisibility(View.GONE);
  }
  
  private void setupRefresher(@NonNull View view) {
    mSrRefresher = view.findViewById(R.id.sr_fragment_movie);
    mSrRefresher.setOnRefreshListener(mMovieViewModel::refresh);
  }
  
  private void setupRecyclerViewAdapter() {
    mMovieListAdapter = new MovieListAdapter(getViewLifecycleOwner(), mMovieViewModel);
    mMovieListAdapter.setMovieItemNavigator(this);
  }
  
  private void setupRecyclerView(@NonNull View view) {
    RecyclerView recyclerView = view.findViewById(R.id.rv_fragment_movie);
    recyclerView.setHasFixedSize(true);
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

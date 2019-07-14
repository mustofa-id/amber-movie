package id.mustofa.app.amber.moviefavorite;


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
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import id.mustofa.app.amber.R;
import id.mustofa.app.amber.data.model.MediaType;
import id.mustofa.app.amber.data.model.MovieFavorite;
import id.mustofa.app.amber.moviedetail.MovieDetailActivity;
import id.mustofa.app.amber.util.ViewModelFactory;

/**
 * @author Habib Mustofa
 * Indonesia on 13/07/19.
 */
public class MovieFavoriteFragment extends Fragment implements MovieFavoriteItemNavigator {
  
  private static final String ARGS_MEDIA_TYPE = "MEDIA_TYPE___";
  private MovieFavoriteGridAdapter mMovieFavoriteGridAdapter;
  private MovieFavoriteViewModel mMovieFavoriteViewModel;
  
  private SwipeRefreshLayout mSrRefresher;
  private TextView mTextInfo;
  
  public static MovieFavoriteFragment newInstance(MediaType type) {
    MovieFavoriteFragment movieFavoriteFragment = new MovieFavoriteFragment();
    Bundle bundle = new Bundle();
    bundle.putSerializable(ARGS_MEDIA_TYPE, type);
    movieFavoriteFragment.setArguments(bundle);
    return movieFavoriteFragment;
  }
  
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_movie_favorite, container, false);
  }
  
  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    setupViewModel();
    setupRecyclerViewAdapter();
    setupInfo(view);
    setupRefresher(view);
    setupRecyclerView(view);
    subscribeViewModelChange();
  }
  
  @Override
  public void onResume() {
    super.onResume();
    mMovieFavoriteViewModel.start();
  }
  
  private void setupViewModel() {
    ViewModelFactory factory = ViewModelFactory.getInstance(getContext());
    mMovieFavoriteViewModel = ViewModelProviders
        .of(this, factory).get(MovieFavoriteViewModel.class);
    mMovieFavoriteViewModel.setMediaType(getMediaType());
  }
  
  private void setupInfo(@NonNull View view) {
    mTextInfo = view.findViewById(R.id.er_fragment_movie_favorite);
    mTextInfo.setVisibility(View.GONE);
  }
  
  private void setupRefresher(@NonNull View view) {
    mSrRefresher = view.findViewById(R.id.sr_fragment_movie_favorite);
    mSrRefresher.setOnRefreshListener(mMovieFavoriteViewModel::refresh);
  }
  
  private void setupRecyclerViewAdapter() {
    mMovieFavoriteGridAdapter = new MovieFavoriteGridAdapter(
        getViewLifecycleOwner(),
        mMovieFavoriteViewModel
    );
    mMovieFavoriteGridAdapter.setMovieFavoriteItemNavigator(this);
  }
  
  private void setupRecyclerView(@NonNull View view) {
    RecyclerView recyclerView = view.findViewById(R.id.rv_fragment_movie_favorite);
    recyclerView.setHasFixedSize(true);
    int orientation = getResources().getConfiguration().orientation;
    int colSpan = orientation == Configuration.ORIENTATION_PORTRAIT ? 3 : 5;
    recyclerView.setLayoutManager(new GridLayoutManager(getContext(), colSpan));
    recyclerView.setAdapter(mMovieFavoriteGridAdapter);
  }
  
  private void subscribeViewModelChange() {
    final LifecycleOwner lifecycleOwner = getViewLifecycleOwner();
    mMovieFavoriteViewModel.getLoading().observe(lifecycleOwner, mSrRefresher::setRefreshing);
    mMovieFavoriteViewModel.getMessageResId().observe(lifecycleOwner, this::onHashMessage);
  }
  
  private void onHashMessage(int message) {
    mTextInfo.setVisibility(message == 0 ? View.GONE : View.VISIBLE);
    if (message != 0) {
      mTextInfo.setText(message);
      Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }
  }
  
  @Override
  public void openMovieFavoriteDetail(MovieFavorite movieFavorite) {
    Intent movieDetailIntent = new Intent(getActivity(), MovieDetailActivity.class);
    movieDetailIntent.putExtra(MovieDetailActivity.EXTRA_MOVIE_ITEM, movieFavorite);
    movieDetailIntent.putExtra(MovieDetailActivity.EXTRA_MOVIE_TYPE, getMediaType());
    startActivity(movieDetailIntent);
  }
  
  private MediaType getMediaType() {
    assert getArguments() != null;
    return (MediaType) getArguments().getSerializable(ARGS_MEDIA_TYPE);
  }
}

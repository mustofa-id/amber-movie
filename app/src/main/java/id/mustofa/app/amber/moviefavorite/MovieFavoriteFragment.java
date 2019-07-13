package id.mustofa.app.amber.moviefavorite;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import id.mustofa.app.amber.R;
import id.mustofa.app.amber.data.model.MediaType;

/**
 * @author Habib Mustofa
 * Indonesia on 13/07/19.
 */
public class MovieFavoriteFragment extends Fragment {
  
  private static final String ARGS_MEDIA_TYPE = "MEDIA_TYPE___";
  
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
    mTextInfo = view.findViewById(R.id.er_fragment_movie_favorite);
    mTextInfo.setText(getMediaType().getValue());
  }
  
  private MediaType getMediaType() {
    assert getArguments() != null;
    return (MediaType) getArguments().getSerializable(ARGS_MEDIA_TYPE);
  }
}

package id.mustofa.app.amber.movie.favorite;

import android.arch.lifecycle.LifecycleOwner;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import id.mustofa.app.amber.R;
import id.mustofa.app.amber.data.model.MovieFavorite;
import id.mustofa.app.amber.util.ImageLoader;

/**
 * @author Habib Mustofa
 * Indonesia on 13/07/19.
 */
public class MovieFavoriteGridAdapter extends RecyclerView.Adapter<MovieFavoriteGridAdapter.MovieFavoriteViewHolder> {
  
  private final List<MovieFavorite> mListMovieFavorite = new ArrayList<>();
  private MovieFavoriteItemNavigator mMovieFavoriteItemNavigator;
  
  MovieFavoriteGridAdapter(@NonNull LifecycleOwner lifecycleOwner,
                           @NonNull MovieFavoriteViewModel viewModel) {
    viewModel.getMovieFavorites().observe(lifecycleOwner, this::populateMovieFavorite);
  }
  
  private void populateMovieFavorite(List<MovieFavorite> movieFavorites) {
    mListMovieFavorite.clear();
    mListMovieFavorite.addAll(movieFavorites);
    notifyDataSetChanged();
  }
  
  void setMovieFavoriteItemNavigator(MovieFavoriteItemNavigator navigator) {
    this.mMovieFavoriteItemNavigator = navigator;
  }
  
  @NonNull
  @Override
  public MovieFavoriteViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
    View view = LayoutInflater.from(viewGroup.getContext())
        .inflate(R.layout.item_movie_favorite, viewGroup, false);
    return new MovieFavoriteViewHolder(view);
  }
  
  @Override
  public void onBindViewHolder(@NonNull MovieFavoriteViewHolder movieFavoriteViewHolder, int i) {
    movieFavoriteViewHolder.setMovieFavorite(mListMovieFavorite.get(i));
  }
  
  @Override
  public int getItemCount() {
    return mListMovieFavorite.size();
  }
  
  class MovieFavoriteViewHolder extends RecyclerView.ViewHolder {
    
    private ImageView poster;
    private TextView title;
    private RatingBar rating;
    
    MovieFavoriteViewHolder(@NonNull View itemView) {
      super(itemView);
      initViews();
    }
    
    private void initViews() {
      poster = itemView.findViewById(R.id.img_item_movie_favorite_poster);
      title = itemView.findViewById(R.id.text_item_movie_favorite_title);
      rating = itemView.findViewById(R.id.rate_item_movie_favorite_rating);
    }
    
    private void setMovieFavorite(MovieFavorite movie) {
      ImageLoader.load(itemView.getContext(), movie.getPosterPath(), poster);
      title.setText(movie.getTitle());
      rating.setRating(movie.getVoteAverage() / 2);
      if (mMovieFavoriteItemNavigator != null) {
        itemView.setOnClickListener(v ->
            mMovieFavoriteItemNavigator.openMovieFavoriteDetail(movie)
        );
      }
    }
  }
}

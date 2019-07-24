package id.mustofa.app.amber.movieall;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import id.mustofa.app.amber.R;
import id.mustofa.app.amber.data.model.MediaType;
import id.mustofa.app.amber.data.model.Movie;
import id.mustofa.app.amber.util.ImageLoader;

/**
 * @author Habib Mustofa
 * Indonesia on 24/07/19.
 */
public class MovieAllListAdapter extends RecyclerView.Adapter<MovieAllListAdapter.MovieAllHolder> {
  
  private final List<Movie> mMovies = new ArrayList<>();
  private final MediaType mMediaType;
  private MovieAllItemNavigator mItemNavigator;
  
  MovieAllListAdapter(@NonNull MediaType type) {
    mMediaType = type;
  }
  
  void setItemNavigator(MovieAllItemNavigator itemNavigator) {
    this.mItemNavigator = itemNavigator;
  }
  
  void populateMovies(List<Movie> movies) {
    mMovies.clear();
    mMovies.addAll(movies);
    notifyDataSetChanged();
  }
  
  boolean isEmpty() {
    return mMovies.isEmpty();
  }
  
  @NonNull
  @Override
  public MovieAllHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
    View view = LayoutInflater.from(viewGroup.getContext())
        .inflate(R.layout.item_movie_all, viewGroup, false);
    return new MovieAllHolder(view);
  }
  
  @Override
  public void onBindViewHolder(@NonNull MovieAllHolder movieAllHolder, int i) {
    movieAllHolder.setMovie(mMovies.get(i));
  }
  
  @Override
  public int getItemCount() {
    return mMovies.size();
  }
  
  @Override
  public long getItemId(int position) {
    return mMovies.get(position).getId();
  }
  
  @Override
  public void setHasStableIds(boolean hasStableIds) {
    super.setHasStableIds(true);
  }
  
  class MovieAllHolder extends RecyclerView.ViewHolder {
    
    private TextView title, rating;
    private ImageView poster;
    
    MovieAllHolder(@NonNull View view) {
      super(view);
      title = view.findViewById(R.id.text_item_movie_all_title);
      rating = view.findViewById(R.id.text_item_movie_all_rating);
      poster = view.findViewById(R.id.img_item_movie_all_poster);
    }
    
    void setMovie(Movie movie) {
      title.setText(movie.getTitle());
      rating.setText(String.valueOf(movie.getVoteAverage()));
      ImageLoader.load(itemView.getContext(), movie.getPosterPath(), poster);
      if (mItemNavigator != null) {
        itemView.setOnClickListener(v -> mItemNavigator.openMovieDetail(movie, mMediaType));
      }
    }
  }
}

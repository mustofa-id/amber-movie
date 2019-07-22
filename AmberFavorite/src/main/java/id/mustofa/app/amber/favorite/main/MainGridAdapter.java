package id.mustofa.app.amber.favorite.main;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import id.mustofa.app.amber.favorite.R;
import id.mustofa.app.amber.favorite.data.model.Movie;
import id.mustofa.app.amber.favorite.util.ImageLoader;

/**
 * @author Habib Mustofa
 * Indonesia on 21/07/19.
 */
class MainGridAdapter extends RecyclerView.Adapter<MainGridAdapter.MovieView> {
  
  private final List<Movie> mMovies = new ArrayList<>();
  private final MainItemNavigator mMainItemNavigator;
  
  MainGridAdapter(MainItemNavigator mainItemNavigator) {
    this.mMainItemNavigator = mainItemNavigator;
  }
  
  void populateData(List<Movie> movies) {
    mMovies.clear();
    mMovies.addAll(movies);
    notifyDataSetChanged();
  }
  
  @NonNull
  @Override
  public MovieView onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
    View view = LayoutInflater.from(viewGroup.getContext())
        .inflate(R.layout.item_movie_main, viewGroup, false);
    return new MovieView(view);
  }
  
  @Override
  public void onBindViewHolder(@NonNull MovieView movieView, int i) {
    movieView.setMovie(mMovies.get(i));
  }
  
  @Override
  public int getItemCount() {
    return mMovies.size();
  }
  
  @Override
  public void setHasStableIds(boolean hasStableIds) {
    super.setHasStableIds(true);
  }
  
  @Override
  public long getItemId(int position) {
    return mMovies.get(position).getId();
  }
  
  class MovieView extends RecyclerView.ViewHolder {
    
    private ImageView poster;
    private TextView title, rating;
    
    MovieView(@NonNull View view) {
      super(view);
      poster = view.findViewById(R.id.img_item_movie_poster);
      title = view.findViewById(R.id.text_item_movie_title);
      rating = view.findViewById(R.id.text_item_movie_rating);
    }
    
    void setMovie(Movie movie) {
      ImageLoader.load(itemView.getContext(), movie.getPosterPath(), poster);
      title.setText(movie.getTitle());
      rating.setText(String.valueOf(movie.getVoteAverage()));
      if (mMainItemNavigator != null) {
        itemView.setOnClickListener(v -> mMainItemNavigator.onItemClick(movie));
      }
    }
  }
}

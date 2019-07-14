package id.mustofa.app.amber.movie;

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
import id.mustofa.app.amber.data.model.Movie;
import id.mustofa.app.amber.util.DateUtil;
import id.mustofa.app.amber.util.ImageLoader;

/**
 * @author Habib Mustofa
 * Indonesia on 06/07/19.
 */
class MovieListAdapter extends RecyclerView.Adapter<MovieListAdapter.MovieViewHolder> {
  
  private final List<Movie> mListMovie = new ArrayList<>();
  private MovieItemNavigator mMovieItemNavigator;
  
  MovieListAdapter(@NonNull LifecycleOwner lifecycleOwner, @NonNull MovieViewModel movieViewModel) {
    movieViewModel.getMovies().observe(lifecycleOwner, this::populateMovie);
  }
  
  private void populateMovie(List<Movie> movies) {
    mListMovie.clear();
    mListMovie.addAll(movies);
    notifyDataSetChanged();
  }
  
  void setMovieItemNavigator(MovieItemNavigator movieItemNavigator) {
    this.mMovieItemNavigator = movieItemNavigator;
  }
  
  @NonNull
  @Override
  public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
    View view = LayoutInflater.from(viewGroup.getContext())
        .inflate(R.layout.item_movie, viewGroup, false);
    return new MovieViewHolder(view);
  }
  
  @Override
  public void onBindViewHolder(@NonNull MovieViewHolder movieViewHolder, int i) {
    Movie movie = mListMovie.get(i);
    if (movie != null) movieViewHolder.setMovie(movie);
  }
  
  @Override
  public void setHasStableIds(boolean hasStableIds) {
    super.setHasStableIds(true);
  }
  
  @Override
  public long getItemId(int position) {
    return mListMovie.get(position).getId();
  }
  
  @Override
  public int getItemCount() {
    return mListMovie.size();
  }
  
  class MovieViewHolder extends RecyclerView.ViewHolder {
    
    private ImageView poster;
    private TextView title, date, rating, overview;
    private RatingBar ratingBar;
    
    MovieViewHolder(@NonNull View itemView) {
      super(itemView);
      initViews();
    }
    
    private void initViews() {
      poster = itemView.findViewById(R.id.img_item_movie_poster);
      title = itemView.findViewById(R.id.text_item_movie_title);
      date = itemView.findViewById(R.id.text_item_movie_date);
      ratingBar = itemView.findViewById(R.id.rate_item_movie_rating);
      rating = itemView.findViewById(R.id.text_item_movie_rating);
      overview = itemView.findViewById(R.id.text_item_movie_overview);
    }
    
    private void setMovie(Movie movie) {
      ImageLoader.load(itemView.getContext(), movie.getPosterPath(), poster);
      title.setText(movie.getTitle());
      date.setText(DateUtil.reformatDate(movie.getReleaseDate(), "MMMM yyyy"));
      ratingBar.setRating(movie.getVoteAverage() / 2); // RatingBar numBars=5 & movie vote max=10
      rating.setText(String.valueOf(movie.getVoteAverage()));
      overview.setText(movie.getOverview());
      
      if (mMovieItemNavigator != null) {
        itemView.setOnClickListener(v -> mMovieItemNavigator.openMovieDetail(movie));
      }
    }
  }
}

package id.mustofa.app.amber.movie.all;

import java.util.List;

import id.mustofa.app.amber.data.model.MediaType;
import id.mustofa.app.amber.data.model.Movie;

/**
 * @author Habib Mustofa
 * Indonesia on 24/07/19.
 */
class MovieAllSectionItem {
  
  private final String title;
  private final MediaType type;
  private final MovieAllListAdapter adapter;
  
  MovieAllSectionItem(String title, MediaType type) {
    this.title = title;
    this.type = type;
    this.adapter = new MovieAllListAdapter(type);
  }
  
  String getTitle() {
    return title;
  }
  
  public MediaType getType() {
    return type;
  }
  
  void populateMovies(List<Movie> movies) {
    adapter.populateMovies(movies);
  }
  
  MovieAllListAdapter getAdapter() {
    return adapter;
  }
}

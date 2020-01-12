package id.mustofa.app.amber.movie.all;

import id.mustofa.app.amber.data.model.MediaType;
import id.mustofa.app.amber.data.model.Movie;

/**
 * @author Habib Mustofa
 * Indonesia on 24/07/19.
 */
public interface MovieAllItemNavigator {
  
  void openMovieDetail(Movie movie, MediaType type);
  
  default void openSectionDetail(MovieAllSectionItem item) {}
}

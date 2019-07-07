package id.mustofa.app.amber.util;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import id.mustofa.app.amber.data.MovieRepository;
import id.mustofa.app.amber.movie.MovieViewModel;
import id.mustofa.app.amber.moviedetail.MovieDetailViewModel;

/**
 * @author Habib Mustofa
 * Indonesia on 06/07/19.
 */
public final class ViewModelFactory extends ViewModelProvider.NewInstanceFactory {
  
  private static volatile ViewModelFactory svInstance;
  
  private final MovieRepository mMovieRepository;
  
  public static ViewModelFactory getInstance() {
    if (svInstance == null) {
      synchronized (ViewModelFactory.class) {
        if (svInstance == null) {
          svInstance = new ViewModelFactory();
        }
      }
    }
    return svInstance;
  }
  
  private ViewModelFactory() {
    this.mMovieRepository = new MovieRepository();
  }
  
  @SuppressWarnings("unused")
  public void destroyInstance() {
    svInstance = null;
  }
  
  @NonNull
  @Override
  @SuppressWarnings("unchecked")
  public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
    if (modelClass.isAssignableFrom(MovieViewModel.class)) {
      return (T) new MovieViewModel(mMovieRepository);
    } else if (modelClass.isAssignableFrom(MovieDetailViewModel.class)) {
      return (T) new MovieDetailViewModel(mMovieRepository);
    }
    throw new IllegalArgumentException("Unknown ViewModel: " + modelClass.getName());
  }
}

package id.mustofa.app.amber.util;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.content.Context;
import android.support.annotation.NonNull;

import id.mustofa.app.amber.data.MovieRepository;
import id.mustofa.app.amber.movie.MovieViewModel;
import id.mustofa.app.amber.movie.all.MovieAllViewModel;
import id.mustofa.app.amber.movie.detail.MovieDetailViewModel;
import id.mustofa.app.amber.movie.favorite.MovieFavoriteViewModel;

/**
 * @author Habib Mustofa
 * Indonesia on 06/07/19.
 */
public final class ViewModelFactory extends ViewModelProvider.NewInstanceFactory {
  
  private static volatile ViewModelFactory svInstance;
  
  private final MovieRepository mMovieRepository;
  
  public static ViewModelFactory getInstance(Context context) {
    if (svInstance == null) {
      synchronized (ViewModelFactory.class) {
        if (svInstance == null) {
          svInstance = new ViewModelFactory(Injection.provideMovieRepository(context));
        }
      }
    }
    return svInstance;
  }
  
  private ViewModelFactory(MovieRepository movieRepository) {
    this.mMovieRepository = movieRepository;
  }
  
  @NonNull
  @Override
  @SuppressWarnings("unchecked")
  public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
    if (modelClass.isAssignableFrom(MovieViewModel.class)) {
      return (T) new MovieViewModel(mMovieRepository);
    } else if (modelClass.isAssignableFrom(MovieDetailViewModel.class)) {
      return (T) new MovieDetailViewModel(mMovieRepository);
    } else if (modelClass.isAssignableFrom(MovieFavoriteViewModel.class)) {
      return (T) new MovieFavoriteViewModel(mMovieRepository);
    } else if (modelClass.isAssignableFrom(MovieAllViewModel.class)) {
      return (T) new MovieAllViewModel(mMovieRepository);
    }
    throw new IllegalArgumentException("Unknown ViewModel: " + modelClass.getName());
  }
}

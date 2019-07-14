package id.mustofa.app.amber.util;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.content.Context;
import android.support.annotation.NonNull;

import id.mustofa.app.amber.data.MovieRepository;
import id.mustofa.app.amber.data.source.local.AppDatabase;
import id.mustofa.app.amber.data.source.local.MovieLocalDao;
import id.mustofa.app.amber.data.source.remote.ApiClient;
import id.mustofa.app.amber.data.source.remote.MovieRemoteDao;
import id.mustofa.app.amber.movie.MovieViewModel;
import id.mustofa.app.amber.moviedetail.MovieDetailViewModel;
import id.mustofa.app.amber.moviefavorite.MovieFavoriteViewModel;

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
          svInstance = new ViewModelFactory(provideMovieRepository(context));
        }
      }
    }
    return svInstance;
  }
  
  private ViewModelFactory(MovieRepository movieRepository) {
    this.mMovieRepository = movieRepository;
  }
  
  private static MovieRepository provideMovieRepository(@NonNull Context context) {
    final MovieRemoteDao movieRemoteDao = ApiClient.retrofit().create(MovieRemoteDao.class);
    final MovieLocalDao movieLocalDao = AppDatabase.getInstance(context).movieLocalDao();
    return new MovieRepository(movieRemoteDao, movieLocalDao);
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
    }
    throw new IllegalArgumentException("Unknown ViewModel: " + modelClass.getName());
  }
}

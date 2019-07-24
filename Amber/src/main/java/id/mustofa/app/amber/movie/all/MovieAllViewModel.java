package id.mustofa.app.amber.movie.all;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import java.util.List;
import java.util.Map;

import id.mustofa.app.amber.data.MovieRepository;
import id.mustofa.app.amber.data.model.MediaType;
import id.mustofa.app.amber.data.model.Movie;

import static id.mustofa.app.amber.util.QueryHelper.Params;
import static id.mustofa.app.amber.util.QueryHelper.createQuery;

/**
 * @author Habib Mustofa
 * Indonesia on 24/07/19.
 */
public class MovieAllViewModel extends ViewModel {
  // Trending, Release today, popular, now playing
  private static final String TAG = MovieAllViewModel.class.getName();
  
  private final MutableLiveData<List<Movie>> mMovieTrending = new MutableLiveData<>();
  private final MutableLiveData<Boolean> mLoading = new MutableLiveData<>();
  private final MutableLiveData<Integer> mMessageResId = new MutableLiveData<>();
  
  private final MovieRepository mMovieRepository;
  
  public MovieAllViewModel(MovieRepository movieRepository) {
    this.mMovieRepository = movieRepository;
  }
  
  void loadTrending() {
    Map<String, String> queryParams = createQuery(
        Params.PAGE, "1"
    );
    
    mMovieRepository.findMovies(MediaType.MOVIE, queryParams, (movieList, error) -> {
      if (error != null) {
        Log.e(TAG, "setLoadTrending: ", error);
      } else {
        mMovieTrending.postValue(movieList);
      }
    });
  }
  
  LiveData<List<Movie>> getMovieTrending() {
    return mMovieTrending;
  }
}

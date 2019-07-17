package id.mustofa.app.amber.movie;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

import id.mustofa.app.amber.R;
import id.mustofa.app.amber.data.MovieRepository;
import id.mustofa.app.amber.data.model.MediaType;
import id.mustofa.app.amber.data.model.Movie;

/**
 * @author Habib Mustofa
 * Indonesia on 06/07/19.
 */
public class MovieViewModel extends ViewModel {
  
  private final MutableLiveData<List<Movie>> mMovies = new MutableLiveData<>();
  private final MutableLiveData<Boolean> mLoading = new MutableLiveData<>();
  private final MutableLiveData<Integer> mMessageResId = new MutableLiveData<>();
  
  private final MovieRepository mMovieRepository;
  
  private MediaType mCurrentMediaType;
  private boolean mIncludeAdult;
  
  public MovieViewModel(MovieRepository mMovieRepository) {
    this.mMovieRepository = mMovieRepository;
    setMediaType(MediaType.MOVIE);
  }
  
  void setMediaType(MediaType type) {
    mCurrentMediaType = type;
  }
  
  void setIncludeAdult(boolean includeAdult) {
    this.mIncludeAdult = includeAdult;
  }
  
  void start() {
    loadMovies(mCurrentMediaType, false);
  }
  
  void refresh() {
    loadMovies(mCurrentMediaType, true);
  }
  
  void searchMovies(String query) {
    mMovies.setValue(new ArrayList<>());
    mMessageResId.postValue(0);
    mLoading.postValue(true);
    mMovieRepository.findMoviesByQuery(mCurrentMediaType, query, (movieList, error) -> {
      if (error != null) {
        if (mMovies.getValue() == null || mMovies.getValue().isEmpty()) {
          mMessageResId.postValue(R.string.msg_failed_fetch_movies);
        }
      } else {
        if (movieList.isEmpty()) {
          mMessageResId.postValue(R.string.msg_no_movie);
        } else {
          mMovies.postValue(movieList);
        }
      }
      mLoading.postValue(false);
    });
  }
  
  private void loadMovies(MediaType type, boolean force) {
    if (force) mMovies.setValue(new ArrayList<>());
    mMessageResId.postValue(0);
    mLoading.postValue(true);
    mMovieRepository.findMovies(type, mIncludeAdult, (movieList, error) -> {
      if (error != null) {
        if (mMovies.getValue() == null || mMovies.getValue().isEmpty()) {
          mMessageResId.postValue(R.string.msg_failed_fetch_movies);
        }
      } else {
        if (movieList.isEmpty()) {
          mMessageResId.postValue(R.string.msg_no_movie);
        } else {
          mMovies.postValue(movieList);
        }
      }
      mLoading.postValue(false);
    });
  }
  
  // LiveData getters
  LiveData<List<Movie>> getMovies() {
    return mMovies;
  }
  
  LiveData<Boolean> getLoading() {
    return mLoading;
  }
  
  LiveData<Integer> getMessageResId() {
    return mMessageResId;
  }
}

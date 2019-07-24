package id.mustofa.app.amber.movie.favorite;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

import id.mustofa.app.amber.R;
import id.mustofa.app.amber.data.MovieRepository;
import id.mustofa.app.amber.data.model.MediaType;
import id.mustofa.app.amber.data.model.MovieFavorite;

/**
 * @author Habib Mustofa
 * Indonesia on 13/07/19.
 */
public class MovieFavoriteViewModel extends ViewModel {
  
  private final MutableLiveData<List<MovieFavorite>> mMovieFavorites = new MutableLiveData<>();
  private final MutableLiveData<Boolean> mLoading = new MutableLiveData<>();
  private final MutableLiveData<Integer> mMessageResId = new MutableLiveData<>();
  
  private final MovieRepository mMovieRepository;
  private MediaType mMediaType;
  
  public MovieFavoriteViewModel(MovieRepository movieRepository) {
    this.mMovieRepository = movieRepository;
    setMediaType(MediaType.MOVIE);
  }
  
  void setMediaType(MediaType type) {
    this.mMediaType = type;
  }
  
  void start() {
    loadMovieFavorites(mMediaType, true);
  }
  
  void refresh() {
    loadMovieFavorites(mMediaType, false);
  }
  
  private void loadMovieFavorites(MediaType type, boolean force) {
    if (force) mMovieFavorites.setValue(new ArrayList<>());
    mMessageResId.postValue(0);
    mLoading.postValue(true);
    mMovieRepository.findFavoriteMovies(type, (movieFavorites, error) -> {
      if (error != null) {
        mMessageResId.postValue(R.string.msg_failed_get_movie_favorite);
      } else {
        if (movieFavorites.isEmpty()) {
          mMessageResId.postValue(type == MediaType.MOVIE ?
              R.string.msg_no_movie_favorite : R.string.msg_no_tv_favorite);
        } else {
          mMovieFavorites.postValue(movieFavorites);
        }
      }
      mLoading.postValue(false);
    });
  }
  
  LiveData<List<MovieFavorite>> getMovieFavorites() {
    return mMovieFavorites;
  }
  
  LiveData<Boolean> getLoading() {
    return mLoading;
  }
  
  LiveData<Integer> getMessageResId() {
    return mMessageResId;
  }
}

package id.mustofa.app.amber.movie.all;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import id.mustofa.app.amber.R;
import id.mustofa.app.amber.data.MovieRepository;
import id.mustofa.app.amber.data.ResultListener;
import id.mustofa.app.amber.data.model.MediaType;
import id.mustofa.app.amber.data.model.Movie;
import id.mustofa.app.amber.util.DateUtil;

import static id.mustofa.app.amber.util.QueryHelper.Params;
import static id.mustofa.app.amber.util.QueryHelper.createQuery;

/**
 * @author Habib Mustofa
 * Indonesia on 24/07/19.
 *
 * Trending, Release today, popular
 */
public class MovieAllViewModel extends ViewModel {
  
  private static final String TAG = MovieAllViewModel.class.getName();
  
  private final MutableLiveData<List<Movie>> mMovieTrending = new MutableLiveData<>();
  private final MutableLiveData<List<Movie>> mTvTrending = new MutableLiveData<>();
  private final MutableLiveData<List<Movie>> mMoviePopular = new MutableLiveData<>();
  private final MutableLiveData<List<Movie>> mTvPopular = new MutableLiveData<>();
  private final MutableLiveData<List<Movie>> mMovieReleaseToday = new MutableLiveData<>();
  
  private final MutableLiveData<Integer> mMessageResId = new MutableLiveData<>();
  
  private final MovieRepository mMovieRepository;
  private String mIncludeAdult;
  
  public MovieAllViewModel(MovieRepository movieRepository) {
    this.mMovieRepository = movieRepository;
  }
  
  void setIncludeAdult(boolean includeAdult) {
    this.mIncludeAdult = String.valueOf(includeAdult);
  }
  
  void loadAll() {
    loadMovieTrending();
    loadTvTrending();
    loadMoviePopular();
    loadTvPopular();
    loadMovieReleaseToday();
  }
  
  private void loadMovieTrending() {
    Map<String, String> queryParams = createQuery(
        Params.INCLUDE_ADULT, mIncludeAdult);
    mMovieRepository.findMoviesTrending(
        MediaType.MOVIE, queryParams, applyListener(mMovieTrending));
  }
  
  private void loadTvTrending() {
    Map<String, String> queryParams = createQuery(
        Params.INCLUDE_ADULT, mIncludeAdult);
    mMovieRepository.findMoviesTrending(
        MediaType.TV, queryParams, applyListener(mTvTrending));
  }
  
  private void loadMoviePopular() {
    Map<String, String> queryParams = createQuery(
        Params.INCLUDE_ADULT, mIncludeAdult);
    mMovieRepository.findMoviesPopular(
        MediaType.MOVIE, queryParams, applyListener(mMoviePopular));
  }
  
  private void loadTvPopular() {
    Map<String, String> queryParams = createQuery(
        Params.INCLUDE_ADULT, mIncludeAdult);
    mMovieRepository.findMoviesPopular(
        MediaType.TV, queryParams, applyListener(mTvPopular));
  }
  
  private void loadMovieReleaseToday() {
    final String today = DateUtil.format(new Date());
    final Map<String, String> queryParams = createQuery(
        Params.INCLUDE_ADULT, mIncludeAdult,
        Params.SORT_BY, "primary_release_date.asc",
        Params.RELEASE_DATE, today
    );
    mMessageResId.postValue(0);
    mMovieRepository.findMovies(MediaType.MOVIE, queryParams, (movieList, error) -> {
      if (error != null) {
        Log.e(TAG, "loadTrending: ", error);
        mMessageResId.postValue(R.string.msg_failed_fetch_some_movies);
      } else {
        Iterator<Movie> moviesIt = movieList.iterator();
        while (moviesIt.hasNext()) {
          Movie movie = moviesIt.next();
          if (!movie.getReleaseDate().equals(today)) {
            moviesIt.remove();
          }
        }
        mMovieReleaseToday.postValue(movieList);
      }
    });
  }
  
  private <T> ResultListener<T> applyListener(MutableLiveData<T> liveData) {
    mMessageResId.postValue(0);
    return (result, error) -> {
      if (error != null) {
        Log.d(TAG, "applyListener: ", error);
        mMessageResId.postValue(R.string.msg_failed_fetch_some_movies);
      } else {
        liveData.postValue(result);
      }
    };
  }
  
  LiveData<List<Movie>> getMovieTrending() {
    return mMovieTrending;
  }
  
  LiveData<List<Movie>> getTvTrending() {
    return mTvTrending;
  }
  
  LiveData<List<Movie>> getMoviePopular() {
    return mMoviePopular;
  }
  
  LiveData<List<Movie>> getTvPopular() {
    return mTvPopular;
  }
  
  LiveData<List<Movie>> getMovieReleaseToday() {
    return mMovieReleaseToday;
  }
  
  LiveData<Integer> getMessageResId() {
    return mMessageResId;
  }
}

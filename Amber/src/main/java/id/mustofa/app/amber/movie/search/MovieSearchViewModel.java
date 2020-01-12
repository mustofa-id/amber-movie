package id.mustofa.app.amber.movie.search;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import id.mustofa.app.amber.R;
import id.mustofa.app.amber.data.MovieRepository;
import id.mustofa.app.amber.data.ResultListener;
import id.mustofa.app.amber.data.model.MediaType;
import id.mustofa.app.amber.data.model.Movie;

import static id.mustofa.app.amber.util.QueryHelper.Params;
import static id.mustofa.app.amber.util.QueryHelper.createQuery;

/**
 * @author Habib Mustofa
 * Indonesia on 25/07/19.
 */
public class MovieSearchViewModel extends ViewModel {
  
  private final MutableLiveData<MovieSearchType> mSearchType = new MutableLiveData<>();
  private final MutableLiveData<List<Movie>> mMovies = new MutableLiveData<>();
  private final MutableLiveData<Boolean> mLoading = new MutableLiveData<>();
  private final MutableLiveData<Integer> mMessageResId = new MutableLiveData<>();
//  private final MutableLiveData<Integer> mCurrentPage = new MutableLiveData<>();
  
  private final MovieRepository mMovieRepository;
  
  private String mIncludeAdult;
  
  public MovieSearchViewModel(MovieRepository mMovieRepository) {
    this.mMovieRepository = mMovieRepository;
    this.mSearchType.setValue(MovieSearchType.MOVIE);
  }
  
  void setIncludeAdult(boolean include) {
    this.mIncludeAdult = String.valueOf(include);
  }
  
  void search(String query) {
//    mCurrentPage.setValue(1);
    mMessageResId.postValue(0);
    mMovies.postValue(new ArrayList<>());
    MovieSearchType type = mSearchType.getValue() == null ?
        MovieSearchType.MOVIE : mSearchType.getValue();
    switch (type) {
      case MOVIE:
        searchRemoteMovies(MediaType.MOVIE, query, 1);
        break;
      case TV:
        searchRemoteMovies(MediaType.TV, query, 1);
        break;
      case FAVORITE:
        searchFavorite(query);
        break;
    }
  }
  
  private void searchRemoteMovies(MediaType type, String query, int page) {
    Map<String, String> queryParams = createQuery(
        Params.QUERY, query,
        Params.INCLUDE_ADULT, mIncludeAdult,
        Params.PAGE, String.valueOf(page)
    );
    mMovieRepository.findMoviesByQuery(type, queryParams, applyListener());
  }
  
  private void searchFavorite(String query) {
    mMovieRepository.findFavoriteByQuery(query, applyListener());
  }
  
  private ResultListener<List<Movie>> applyListener() {
    mMessageResId.postValue(0);
    mLoading.postValue(true);
    return (movieList, error) -> {
      if (error != null) {
        if (mMovies.getValue() == null || mMovies.getValue().isEmpty()) {
          mMessageResId.postValue(R.string.msg_failed_fetch_movies);
        }
      } else {
        if (movieList.isEmpty()) mMessageResId.postValue(R.string.msg_no_movie);
        mMovies.postValue(movieList);
      }
      mLoading.postValue(false);
    };
  }
  
  void setSearchType(MovieSearchType searchType) {
    mSearchType.postValue(searchType);
  }
  
  // LiveData getters
  LiveData<List<Movie>> getMovieResult() {
    return mMovies;
  }
  
  LiveData<Integer> getMessageResId() {
    return mMessageResId;
  }
  
  LiveData<Boolean> getLoading() {
    return mLoading;
  }
  
  LiveData<MovieSearchType> getSearchType() {
    return mSearchType;
  }

//  LiveData<Integer> getCurrentPage() {
//    return mCurrentPage;
//  }
}

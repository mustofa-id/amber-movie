package id.mustofa.app.amber.movie.detail;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import id.mustofa.app.amber.R;
import id.mustofa.app.amber.data.MovieRepository;
import id.mustofa.app.amber.data.model.Genre;
import id.mustofa.app.amber.data.model.MediaType;
import id.mustofa.app.amber.data.model.Movie;

import static id.mustofa.app.amber.util.QueryHelper.Params;
import static id.mustofa.app.amber.util.QueryHelper.createQuery;

/**
 * @author Habib Mustofa
 * Indonesia on 06/07/19.
 */
public class MovieDetailViewModel extends ViewModel {
  
  private final MutableLiveData<List<Genre>> mGenres = new MutableLiveData<>();
  private final MutableLiveData<List<Movie>> mSimilarMovies = new MutableLiveData<>();
  private final MutableLiveData<Integer> mGenresMessageResId = new MutableLiveData<>();
  private final MutableLiveData<Boolean> mIsFavorite = new MutableLiveData<>();
  private final MutableLiveData<Integer> mIsFavoriteMessageResId = new MutableLiveData<>();
  private final MutableLiveData<Integer> mSimilarMovieMessageResId = new MutableLiveData<>();
  
  private final MovieRepository mMovieRepository;
  
  private MediaType mMediaType;
  
  public MovieDetailViewModel(MovieRepository movieRepository) {
    this.mMovieRepository = movieRepository;
    setMediaType(MediaType.MOVIE);
  }
  
  void setMediaType(MediaType type) {
    mMediaType = type;
  }
  
  void addToFavorite(Movie movie) {
    movie.setType(mMediaType.getValue());
    mMovieRepository.addToFavorite(movie, (id, error) -> {
      if (error != null || id == -1) {
        mIsFavoriteMessageResId.postValue(R.string.msg_add_favorite_fail);
      } else {
        mIsFavoriteMessageResId.postValue(R.string.msg_add_favorite_ok);
        checkFavorite(movie.getId());
      }
    });
  }
  
  void removeFromFavorite(Movie movie) {
    mMovieRepository.removeFromFavorite(movie, (row, error) -> {
      if (error != null || row == -1) {
        mIsFavoriteMessageResId.postValue(R.string.msg_remove_favorite_fail);
      } else {
        mIsFavoriteMessageResId.postValue(R.string.msg_remove_favorite_ok);
        checkFavorite(movie.getId());
      }
    });
  }
  
  void checkFavorite(long movieId) {
    mMovieRepository.isInFavorite(movieId,
        (isFavorite, error) -> mIsFavorite.postValue(error != null || !isFavorite));
  }
  
  void setGenreIds(@NonNull List<Long> genreIds) {
    mGenresMessageResId.postValue(R.string.msg_loading);
    mMovieRepository.findGenres(mMediaType, (genres, error) -> {
      if (error != null) {
        mGenresMessageResId.postValue(R.string.msg_failed_fetch_genres);
      } else {
        if (genres.isEmpty()) {
          mGenresMessageResId.postValue(R.string.msg_no_genre);
        } else {
          List<Genre> genresToPost = new ArrayList<>();
          for (Genre genre : genres) {
            for (long id : genreIds) {
              if (genre.getId() == id) {
                genresToPost.add(genre);
                break;
              }
            }
          }
          mGenres.postValue(genresToPost);
          mGenresMessageResId.postValue(0);
        }
      }
    });
  }
  
  void loadSimilar(long movieId, boolean includeAdult) {
    Map<String, String> params = createQuery(
        Params.INCLUDE_ADULT, String.valueOf(includeAdult)
    );
    mMovieRepository.findMoviesSimilar(mMediaType, movieId, params, (result, error) -> {
      if (error != null) {
        mSimilarMovieMessageResId.postValue(R.string.msg_similar_movie_err);
      } else {
        mSimilarMovies.postValue(result);
      }
    });
  }
  
  LiveData<List<Genre>> getGenres() {
    return mGenres;
  }
  
  LiveData<List<Movie>> getSimilarMovie() {
    return mSimilarMovies;
  }
  
  LiveData<Integer> getGenresMessageResId() {
    return mGenresMessageResId;
  }
  
  LiveData<Boolean> getIsFavorite() {
    return mIsFavorite;
  }
  
  LiveData<Integer> getFavoriteMessageResId() {
    return mIsFavoriteMessageResId;
  }
  
  LiveData<Integer> getSimilarMovieMessageResId() {
    return mSimilarMovieMessageResId;
  }
}

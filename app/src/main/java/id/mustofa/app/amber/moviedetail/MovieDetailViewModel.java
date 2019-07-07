package id.mustofa.app.amber.moviedetail;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import id.mustofa.app.amber.R;
import id.mustofa.app.amber.data.MovieRepository;
import id.mustofa.app.amber.data.model.Genre;
import id.mustofa.app.amber.data.model.MediaType;

/**
 * @author Habib Mustofa
 * Indonesia on 06/07/19.
 */
public class MovieDetailViewModel extends ViewModel {
  
  private final MutableLiveData<List<Genre>> mGenres = new MutableLiveData<>();
  private final MutableLiveData<Integer> mMessageResId = new MutableLiveData<>();
  
  private final MovieRepository mMovieRepository;
  
  private MediaType mMediaType;
  
  public MovieDetailViewModel(MovieRepository movieRepository) {
    this.mMovieRepository = movieRepository;
    setMediaType(MediaType.MOVIE);
  }
  
  void setMediaType(MediaType type) {
    mMediaType = type;
  }
  
  void setGenreIds(@NonNull List<Long> genreIds) {
    mMessageResId.postValue(R.string.msg_loading);
    mMovieRepository.findGenres(mMediaType, (genres, error) -> {
      if (error != null) {
        mMessageResId.postValue(R.string.msg_failed_fetch_genres);
      } else {
        if (genres.isEmpty()) {
          mMessageResId.postValue(R.string.msg_no_genre);
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
          mMessageResId.postValue(0);
        }
      }
    });
  }
  
  LiveData<List<Genre>> getGenres() {
    return mGenres;
  }
  
  LiveData<Integer> getMessageResId() {
    return mMessageResId;
  }
}

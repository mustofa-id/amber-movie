package id.mustofa.app.amber.util;

import android.content.Context;
import android.support.annotation.NonNull;

import id.mustofa.app.amber.data.MovieRepository;
import id.mustofa.app.amber.data.source.local.AppDatabase;
import id.mustofa.app.amber.data.source.local.MovieLocalDao;
import id.mustofa.app.amber.data.source.remote.ApiClient;
import id.mustofa.app.amber.data.source.remote.MovieRemoteDao;

/**
 * @author Habib Mustofa
 * Indonesia on 19/07/19.
 */
public class Injection {
  
  public static MovieRepository provideMovieRepository(@NonNull Context context) {
    final MovieRemoteDao movieRemoteDao = ApiClient.retrofit().create(MovieRemoteDao.class);
    final MovieLocalDao movieLocalDao = AppDatabase.getInstance(context).movieLocalDao();
    return new MovieRepository(movieRemoteDao, movieLocalDao);
  }
}

package id.mustofa.app.amber.data;

import android.support.annotation.NonNull;

import java.util.List;

/**
 * @author Habib Mustofa
 * Indonesia on 06/07/19.
 */
public interface ResultListener<T> {
  
  void onResult(@NonNull T result, Throwable error);
}
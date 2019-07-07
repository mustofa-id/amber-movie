package id.mustofa.app.amber.data.model;

import android.support.annotation.NonNull;


/**
 * @author Habib Mustofa
 * Indonesia on 05/07/19.
 */
public enum MediaType {
  
  MOVIE("movie"), TV("tv");
  
  private final String value;
  
  MediaType(String value) {this.value = value;}
  
  @NonNull
  public String getValue() {
    return value;
  }
}

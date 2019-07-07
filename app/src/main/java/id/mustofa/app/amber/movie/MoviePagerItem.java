package id.mustofa.app.amber.movie;

import android.support.v4.app.Fragment;

/**
 * @author Habib Mustofa
 * Indonesia on 06/07/19.
 */
final class MoviePagerItem {
  
  private String title;
  private Fragment fragment;
  
  MoviePagerItem(String title, Fragment fragment) {
    this.title = title;
    this.fragment = fragment;
  }
  
  String getTitle() {
    return title;
  }
  
  Fragment getFragment() {
    return fragment;
  }
}

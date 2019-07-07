package id.mustofa.app.amber.movie;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * @author Habib Mustofa
 * Indonesia on 06/07/19.
 */
class MoviePagerAdapter extends FragmentPagerAdapter {
  
  private final List<MoviePagerItem> mMoviePagerItemList;
  
  MoviePagerAdapter(FragmentManager fragmentManager, List<MoviePagerItem> mMoviePagerItemList) {
    super(fragmentManager);
    this.mMoviePagerItemList = mMoviePagerItemList;
  }
  
  @Override
  public Fragment getItem(int position) {
    MoviePagerItem moviePagerItem = mMoviePagerItemList.get(position);
    return moviePagerItem.getFragment();
  }
  
  @Nullable
  @Override
  public CharSequence getPageTitle(int position) {
    MoviePagerItem moviePagerItem = mMoviePagerItemList.get(position);
    return moviePagerItem.getTitle();
  }
  
  @Override
  public int getCount() {
    return mMoviePagerItemList.size();
  }
}
package id.mustofa.app.amber.main;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

/**
 * @author Habib Mustofa
 * Indonesia on 06/07/19.
 */
class MainPagerAdapter extends FragmentStatePagerAdapter {
  
  private final List<MainPagerItem> mMainPagerItemList;
  private boolean mNeedUpdate;
  
  MainPagerAdapter(FragmentManager fragmentManager, List<MainPagerItem> mMainPagerItemList) {
    super(fragmentManager);
    this.mMainPagerItemList = mMainPagerItemList;
  }
  
  @Override
  public Fragment getItem(int position) {
    MainPagerItem mainPagerItem = mMainPagerItemList.get(position);
    return mainPagerItem.getFragment();
  }
  
  @Nullable
  @Override
  public CharSequence getPageTitle(int position) {
    MainPagerItem mainPagerItem = mMainPagerItemList.get(position);
    return mainPagerItem.getTitle();
  }
  
  @Override
  public int getItemPosition(@NonNull Object object) {
    return mNeedUpdate ? POSITION_NONE : super.getItemPosition(object);
  }
  
  @Override
  public int getCount() {
    return mMainPagerItemList.size();
  }
  
  @Override
  public void notifyDataSetChanged() {
    mNeedUpdate = true;
    super.notifyDataSetChanged();
    mNeedUpdate = false;
  }
}
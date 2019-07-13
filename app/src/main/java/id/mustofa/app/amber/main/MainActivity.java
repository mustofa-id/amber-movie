package id.mustofa.app.amber.main;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import id.mustofa.app.amber.R;
import id.mustofa.app.amber.base.BaseAppCompatActivity;
import id.mustofa.app.amber.data.model.MediaType;
import id.mustofa.app.amber.movie.MovieFragment;
import id.mustofa.app.amber.moviefavorite.MovieFavoriteFragment;
import id.mustofa.app.amber.setting.SettingActivity;

/**
 * @author Habib Mustofa
 * Indonesia on 06/07/19.
 */
public class MainActivity extends BaseAppCompatActivity {
  
  private static final String KEY_CURRENT_MODE = "CURRENT_MODE__";
  
  private boolean mCloseable;
  private int mCurrentMovieMode;
  private List<MainPagerItem> mPagerItems;
  private MainPagerAdapter mMainPagerAdapter;
  
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    if (savedInstanceState != null) {
      mCurrentMovieMode = savedInstanceState.getInt(KEY_CURRENT_MODE, 0);
    }
    setupToolbar();
    updateTitle();
    setupPager();
    updatePagerItems();
  }
  
  private void setupToolbar() {
    Toolbar toolbar = findViewById(R.id.toolbar_main);
    setSupportActionBar(toolbar);
  }
  
  private void updateTitle() {
    ActionBar actionBar = getSupportActionBar();
    if (actionBar != null) {
      actionBar.setTitle(mCurrentMovieMode == 0 ? "Discover" : "Favorite");
    }
  }
  
  private void setupPager() {
    ViewPager viewPager = findViewById(R.id.view_pager);
    TabLayout tabLayout = findViewById(R.id.tabs);
    mPagerItems = new ArrayList<>();
    mMainPagerAdapter = new MainPagerAdapter(getSupportFragmentManager(), mPagerItems);
    viewPager.setAdapter(mMainPagerAdapter);
    tabLayout.setupWithViewPager(viewPager);
  }
  
  private void updatePagerItems() {
    mPagerItems.clear();
    List<MainPagerItem> pagerItems = mCurrentMovieMode == 0
        ? getMoviePagerItems() : getMovieFavoritePagerItems();
    mPagerItems.addAll(pagerItems);
    mMainPagerAdapter.notifyDataSetChanged();
  }
  
  private List<MainPagerItem> getMoviePagerItems() {
    return Arrays.asList(
        new MainPagerItem(getString(R.string.title_tab_movie), MovieFragment.newInstance(MediaType.MOVIE)),
        new MainPagerItem(getString(R.string.title_tab_tv), MovieFragment.newInstance(MediaType.TV))
    );
  }
  
  private List<MainPagerItem> getMovieFavoritePagerItems() {
    return Arrays.asList(
        new MainPagerItem(getString(R.string.title_tab_movie), MovieFavoriteFragment.newInstance(MediaType.MOVIE)),
        new MainPagerItem(getString(R.string.title_tab_tv), MovieFavoriteFragment.newInstance(MediaType.TV))
    );
  }
  
  @Override
  public void onBackPressed() {
    if (mCloseable) super.onBackPressed();
    Toast.makeText(this, R.string.msg_confirm_close, Toast.LENGTH_SHORT).show();
    new Handler().postDelayed(() -> mCloseable = false, 2000);
    mCloseable = true;
  }
  
  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.menu_main, menu);
    return super.onCreateOptionsMenu(menu);
  }
  
  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    if (item.getItemId() == R.id.menu_main_settings) {
      Intent settingIntent = new Intent(this, SettingActivity.class);
      // Get result from SettingActivity.
      // If some change need view to update or recreate
      startActivityForResult(settingIntent, 0);
    }
    return super.onOptionsItemSelected(item);
  }
  
  @Override
  protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
    if (resultCode == SettingActivity.RESULT_NEED_UPDATE) {
      // Here the result code need this view update or recreate
      recreate();
    }
    super.onActivityResult(requestCode, resultCode, data);
  }
  
  @Override
  protected void onSaveInstanceState(Bundle outState) {
    outState.putInt(KEY_CURRENT_MODE, mCurrentMovieMode);
    super.onSaveInstanceState(outState);
  }
}
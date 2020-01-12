package id.mustofa.app.amber.main;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
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
import id.mustofa.app.amber.movie.all.MovieAllFragment;
import id.mustofa.app.amber.movie.favorite.MovieFavoriteFragment;
import id.mustofa.app.amber.movie.search.MovieSearchActivity;
import id.mustofa.app.amber.setting.SettingActivity;

/**
 * @author Habib Mustofa
 * Indonesia on 06/07/19.
 */
public class MainActivity extends BaseAppCompatActivity
    implements NavigationView.OnNavigationItemSelectedListener {
  
  private static final String KEY_CURRENT_MODE = "CURRENT_MODE__";
  
  private boolean mCloseable;
  private int mCurrentMovieMode;
  private List<MainPagerItem> mPagerItems;
  private MainPagerAdapter mPagerAdapter;
  private DrawerLayout mDrawer;
  private Toolbar mToolbar;
  private NavigationView mNavigationView;
  
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    setupToolbar();
    setupPager();
    setupDrawer();
    if (savedInstanceState != null) {
      mCurrentMovieMode = savedInstanceState.getInt(KEY_CURRENT_MODE, 0);
    }
    setCurrentMovieMode(mCurrentMovieMode);
  }
  
  private void setupToolbar() {
    mToolbar = findViewById(R.id.toolbar_main);
    setSupportActionBar(mToolbar);
  }
  
  private void updateTitle() {
    ActionBar actionBar = getSupportActionBar();
    if (actionBar != null) {
      actionBar.setTitle(mCurrentMovieMode == 0 ? "Discover" : "Favorite");
    }
  }
  
  private void setupDrawer() {
    mDrawer = findViewById(R.id.drawer_layout_main);
    mNavigationView = findViewById(R.id.nav_main);
    ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,
        mDrawer, mToolbar, R.string.desc_drawer_open, R.string.desc_drawer_close);
    mDrawer.addDrawerListener(toggle);
    toggle.syncState();
    mNavigationView.setNavigationItemSelectedListener(this);
  }
  
  private void setupPager() {
    ViewPager viewPager = findViewById(R.id.view_pager);
    TabLayout tabLayout = findViewById(R.id.tabs);
    mPagerItems = new ArrayList<>();
    mPagerAdapter = new MainPagerAdapter(getSupportFragmentManager(), mPagerItems);
    viewPager.setAdapter(mPagerAdapter);
    tabLayout.setupWithViewPager(viewPager);
  }
  
  private void updatePagerItems() {
    mPagerItems.clear();
    List<MainPagerItem> pagerItems = mCurrentMovieMode == 0
        ? getMoviePagerItems() : getMovieFavoritePagerItems();
    mPagerItems.addAll(pagerItems);
    mPagerAdapter.notifyDataSetChanged();
  }
  
  private void setCurrentMovieMode(int mode) {
    mCurrentMovieMode = mode;
    mNavigationView
        .getMenu()
        .getItem(mode)
        .setChecked(true);
    updateTitle();
    updatePagerItems();
  }
  
  private List<MainPagerItem> getMoviePagerItems() {
    return Arrays.asList(
        new MainPagerItem(getString(R.string.title_tab_all), MovieAllFragment.newInstance()),
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
    getMenuInflater().inflate(R.menu.menu_activity_main, menu);
    return super.onCreateOptionsMenu(menu);
  }
  
  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    if (item.getItemId() == R.id.act_main_search) openSearch();
    return super.onOptionsItemSelected(item);
  }
  
  @Override
  public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
    int id = menuItem.getItemId();
    if (id == R.id.nav_main_discover) {
      setCurrentMovieMode(0);
    } else if (id == R.id.nav_main_favorite) {
      setCurrentMovieMode(1);
    } else if (id == R.id.nav_main_search) {
      openSearch();
    } else if (id == R.id.nav_main_setting) {
      Intent settingIntent = new Intent(this, SettingActivity.class);
      // Get result from SettingActivity.
      // If some change need view to update or recreate
      startActivityForResult(settingIntent, 0);
    }
    mDrawer.closeDrawer(GravityCompat.START);
    return false;
  }
  
  @Override
  protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
    if (resultCode == SettingActivity.RESULT_NEED_UPDATE) {
      // Here the result code need this view update or recreate
      recreate();
    }
    super.onActivityResult(requestCode, resultCode, data);
  }
  
  private void openSearch() {
    Intent intent = new Intent(this, MovieSearchActivity.class);
    startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
  }
  
  @Override
  protected void onSaveInstanceState(Bundle outState) {
    outState.putInt(KEY_CURRENT_MODE, mCurrentMovieMode);
    super.onSaveInstanceState(outState);
  }
}
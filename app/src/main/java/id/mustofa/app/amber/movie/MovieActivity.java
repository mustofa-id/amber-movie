package id.mustofa.app.amber.movie;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.Arrays;
import java.util.List;

import id.mustofa.app.amber.R;
import id.mustofa.app.amber.base.BaseAppCompatActivity;
import id.mustofa.app.amber.data.model.MediaType;
import id.mustofa.app.amber.setting.SettingActivity;

/**
 * @author Habib Mustofa
 * Indonesia on 06/07/19.
 */
public class MovieActivity extends BaseAppCompatActivity {
  
  private boolean mCloseable;
  
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_movie);
    setupToolbar();
    setupPager();
  }
  
  private void setupToolbar() {
    Toolbar toolbar = findViewById(R.id.toolbar_main);
    setSupportActionBar(toolbar);
  }
  
  private void setupPager() {
    MoviePagerAdapter moviePagerAdapter = new MoviePagerAdapter(getSupportFragmentManager(), getMoviePagerItems());
    ViewPager viewPager = findViewById(R.id.view_pager);
    viewPager.setAdapter(moviePagerAdapter);
    TabLayout tabs = findViewById(R.id.tabs);
    tabs.setupWithViewPager(viewPager);
  }
  
  private List<MoviePagerItem> getMoviePagerItems() {
    return Arrays.asList(
        new MoviePagerItem(getString(R.string.title_tab_movie), MovieFragment.newInstance(MediaType.MOVIE)),
        new MoviePagerItem(getString(R.string.title_tab_tv), MovieFragment.newInstance(MediaType.TV))
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
      startActivityForResult(settingIntent, 1);
    }
    return super.onOptionsItemSelected(item);
  }
  
  @Override
  protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
    if (resultCode == RESULT_OK) finish();
    super.onActivityResult(requestCode, resultCode, data);
  }
}
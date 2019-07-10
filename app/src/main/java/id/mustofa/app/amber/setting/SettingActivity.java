package id.mustofa.app.amber.setting;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;

import id.mustofa.app.amber.R;
import id.mustofa.app.amber.base.BaseAppCompatActivity;

/**
 * @author Habib Mustofa
 * Indonesia on 06/07/19.
 */
public class SettingActivity extends BaseAppCompatActivity implements SettingActionListener {
  
  public static final int RESULT_NEED_UPDATE = 868;
  
  private static final String KEY_NEED_UPDATE = "_NEED_UPDATE_";
  
  private boolean mViewNeedUpdate;
  
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_setting);
    
    if (savedInstanceState != null) {
      mViewNeedUpdate = savedInstanceState.getBoolean(KEY_NEED_UPDATE);
    }
    
    setupActionBar();
    addSettingFragment();
  }
  
  private void setupActionBar() {
    ActionBar actionBar = getSupportActionBar();
    if (actionBar != null) {
      actionBar.setDisplayHomeAsUpEnabled(true);
      actionBar.setTitle(R.string.title_setting);
    }
  }
  
  private void addSettingFragment() {
    final FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
    transaction.add(R.id.fl_setting_parent, new SettingFragment());
    transaction.commit();
  }
  
  @Override
  public void onBackPressed() {
    if (mViewNeedUpdate) setResult(RESULT_NEED_UPDATE);
    super.onBackPressed();
  }
  
  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    if (item.getItemId() == android.R.id.home) onBackPressed();
    return super.onOptionsItemSelected(item);
  }
  
  @Override
  public void onPreferenceChanged(boolean forceViewUpdate) {
    this.mViewNeedUpdate = forceViewUpdate;
    if (forceViewUpdate) recreate();
  }
  
  @Override
  protected void onSaveInstanceState(Bundle outState) {
    // Hold mViewNeedUpdate value to overcome change
    // due onPreferenceChanged trigger recreate method
    outState.putBoolean(KEY_NEED_UPDATE, mViewNeedUpdate);
    super.onSaveInstanceState(outState);
  }
}

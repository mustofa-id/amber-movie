package id.mustofa.app.amber.splash;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;

import id.mustofa.app.amber.R;
import id.mustofa.app.amber.main.MainActivity;

/**
 * @author Habib Mustofa
 * Indonesia on 06/07/19.
 */
public class SplashActivity extends AppCompatActivity {
  
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setupDefaultPrefs();
    startMovieActivity();
  }
  
  private void setupDefaultPrefs() {
    final String initializedKey = "initialized";
    final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
    if (!prefs.contains(initializedKey)) {
      SharedPreferences.Editor editor = prefs.edit();
      editor.putBoolean(initializedKey, true);
      editor.putString(getString(R.string.key_prefs_lang),
          getResources().getStringArray(R.array.prefs_entryValues_lang)[0]);
      editor.putBoolean(getString(R.string.key_prefs_daily_reminder), true);
      editor.putBoolean(getString(R.string.key_prefs_release_today), true);
      editor.putBoolean(getString(R.string.key_prefs_restricted_mode), true);
      editor.apply();
    }
  }
  
  private void startMovieActivity() {
    startActivity(new Intent(this, MainActivity.class));
    finish();
  }
}

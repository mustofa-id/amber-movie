package id.mustofa.app.amber.splash;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;

import id.mustofa.app.amber.R;
import id.mustofa.app.amber.main.MainActivity;
import id.mustofa.app.amber.notification.NotificationAction;

/**
 * @author Habib Mustofa
 * Indonesia on 06/07/19.
 */
public class SplashActivity extends AppCompatActivity {
  
  private static final String PREFS_INITIAL_KEY = "initialized";
  private static final boolean PREFS_DEFAULT_DAILY_REMINDER = true;
  private static final boolean PREFS_DEFAULT_RELEASE_TODAY = true;
  private static final boolean PREFS_DEFAULT_RESTRICTED_MODE = true;
  
  private boolean mIsFirstSetup = false;
  
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setupDefaultPrefs();
    setupNotification();
    startMovieActivity();
  }
  
  private void setupDefaultPrefs() {
    final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
    if (!prefs.contains(PREFS_INITIAL_KEY)) {
      SharedPreferences.Editor editor = prefs.edit();
      editor.putBoolean(PREFS_INITIAL_KEY, true);
      editor.putString(getString(R.string.key_prefs_lang),
          getResources().getStringArray(R.array.prefs_entryValues_lang)[0]);
      editor.putBoolean(getString(R.string.key_prefs_daily_reminder), PREFS_DEFAULT_DAILY_REMINDER);
      editor.putBoolean(getString(R.string.key_prefs_release_today), PREFS_DEFAULT_RELEASE_TODAY);
      editor.putBoolean(getString(R.string.key_prefs_restricted_mode), PREFS_DEFAULT_RESTRICTED_MODE);
      editor.apply();
      mIsFirstSetup = true;
    }
  }
  
  private void setupNotification() {
    if (mIsFirstSetup) {
      NotificationAction notificationAction = new NotificationAction(this);
      notificationAction.setDailyReminderEnabled(PREFS_DEFAULT_DAILY_REMINDER);
      notificationAction.setReleaseTodayEnabled(PREFS_DEFAULT_RELEASE_TODAY);
    }
  }
  
  private void startMovieActivity() {
    startActivity(new Intent(this, MainActivity.class));
    finish();
  }
}

package id.mustofa.app.amber.setting;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.preference.PreferenceFragmentCompat;

import id.mustofa.app.amber.R;
import id.mustofa.app.amber.notification.NotificationAction;

/**
 * @author Habib Mustofa
 * Indonesia on 10/07/19.
 */
public class SettingFragment extends PreferenceFragmentCompat {
  
  private String mKeyLang, mKeyDailyReminder, mKeyReleaseToday, mKeyRestrictedMode;
  private SharedPreferences mPreferences;
  private SettingActionListener mActionListener;
  private NotificationAction mNotificationAction;
  
  //  SharedPreferences using WeakHashMap for listener. It work at first, but sometimes not because
  //  listener will get garbage collected if we use listener as anonymous inner class.
  //  Official docs said to `keep a reference to the listener in the instance data of an object.`
  //
  //  Docs: https://developer.android.com/reference/android/content/SharedPreferences.html
  private SharedPreferences.OnSharedPreferenceChangeListener mPrefsChangeListener = (prefs, key) -> {
    if (mNotificationAction != null) {
      if (key.equals(mKeyDailyReminder)) {
        mNotificationAction.setDailyReminderEnabled(prefs.getBoolean(mKeyDailyReminder, false));
      } else if (key.equals(mKeyReleaseToday)) {
        mNotificationAction.setReleaseTodayEnabled(prefs.getBoolean(mKeyReleaseToday, false));
      }
    }
    
    final boolean forceViewUpdate = getActivity() != null &&
        (key.equals(mKeyLang) || key.equals(mKeyRestrictedMode));
    if (mActionListener != null) mActionListener.onPreferenceChanged(forceViewUpdate);
  };
  
  @Override
  public void onAttach(Context context) {
    super.onAttach(context);
    if (context instanceof SettingActionListener) {
      this.mActionListener = (SettingActionListener) context;
    }
  }
  
  @Override
  public void onCreatePreferences(Bundle bundle, String s) {
    addPreferencesFromResource(R.xml.preferences);
    mPreferences = getPreferenceScreen().getSharedPreferences();
    setupPreferences();
    setupNotificationAction();
  }
  
  private void setupNotificationAction() {
    if (getContext() != null) {
      mNotificationAction = new NotificationAction(getContext());
    }
  }
  
  @Override
  public void onResume() {
    super.onResume();
    mPreferences.registerOnSharedPreferenceChangeListener(mPrefsChangeListener);
  }
  
  private void setupPreferences() {
    mKeyLang = getString(R.string.key_prefs_lang);
    mKeyDailyReminder = getString(R.string.key_prefs_daily_reminder);
    mKeyReleaseToday = getString(R.string.key_prefs_release_today);
    mKeyRestrictedMode = getString(R.string.key_prefs_restricted_mode);
  }
  
  @Override
  public void onPause() {
    super.onPause();
    mPreferences.unregisterOnSharedPreferenceChangeListener(mPrefsChangeListener);
  }
  
  @Override
  public void onDetach() {
    super.onDetach();
    this.mActionListener = null;
  }
}

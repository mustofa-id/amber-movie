package id.mustofa.app.amber.setting;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.preference.PreferenceFragmentCompat;

import id.mustofa.app.amber.R;

/**
 * @author Habib Mustofa
 * Indonesia on 10/07/19.
 */
public class SettingFragment extends PreferenceFragmentCompat {
  
  private String mKeyLang, mKeyRestrictedMode;
  private SharedPreferences mPreferences;
  private SettingActionListener mActionListener;
  
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
  }
  
  private void setupPreferences() {
    mKeyLang = getString(R.string.key_prefs_lang);
    mKeyRestrictedMode = getString(R.string.key_prefs_restricted_mode);
  }
  
  @Override
  public void onResume() {
    super.onResume();
    mPreferences.registerOnSharedPreferenceChangeListener(this::onPrefsChanged);
  }
  
  @Override
  public void onPause() {
    super.onPause();
    mPreferences.unregisterOnSharedPreferenceChangeListener(this::onPrefsChanged);
  }
  
  private void onPrefsChanged(@SuppressWarnings("unused") SharedPreferences prefs, String key) {
    final boolean forceViewUpdate = getActivity() != null &&
        (key.equals(mKeyLang) || key.equals(mKeyRestrictedMode));
    if (mActionListener != null) mActionListener.onPreferenceChanged(forceViewUpdate);
  }
  
  @Override
  public void onDetach() {
    super.onDetach();
    this.mActionListener = null;
  }
}

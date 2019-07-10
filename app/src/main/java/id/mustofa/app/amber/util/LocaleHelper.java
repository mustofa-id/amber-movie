package id.mustofa.app.amber.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.v7.preference.PreferenceManager;
import android.util.DisplayMetrics;

import java.util.Locale;

import id.mustofa.app.amber.R;

/**
 * @author Habib Mustofa
 * Indonesia on 06/07/19.
 */
public class LocaleHelper {
  
  public static void updateLocale(@NonNull Context context) {
    final String defaultLang = context.getResources().getStringArray(R.array.prefs_entryValues_lang)[0];
    final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
    final String lang = prefs.getString(context.getString(R.string.key_prefs_lang), defaultLang);
    
    Resources resources = context.getResources();
    Locale locale = new Locale(lang);
    Locale.setDefault(locale);
    
    Configuration configuration = new Configuration();
    configuration.setLocale(locale);
    
    DisplayMetrics displayMetrics = resources.getDisplayMetrics();
    resources.updateConfiguration(configuration, displayMetrics);
  }
}

package id.mustofa.app.amber.util;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;

import java.util.Locale;

/**
 * @author Habib Mustofa
 * Indonesia on 06/07/19.
 */
public class LocaleHelper {
  
  private static LocaleHelper sInstance;
  
  private String mCurrentLocale;
  
  private LocaleHelper() {}
  
  public static LocaleHelper getInstance() {
    if (sInstance == null) {
      synchronized (LocaleHelper.class) {
        if (sInstance == null) {
          sInstance = new LocaleHelper();
        }
      }
    }
    return sInstance;
  }
  
  public void updateLocale(@NonNull Context context) {
    if (mCurrentLocale == null || mCurrentLocale.isEmpty()) return;
    updateLocale(context, mCurrentLocale);
  }
  
  public void updateLocale(@NonNull Context context, @NonNull String langId) {
    mCurrentLocale = langId;
    Resources resources = context.getResources();
    
    Locale locale = new Locale(langId);
    Locale.setDefault(locale);
    
    Configuration configuration = new Configuration();
    configuration.setLocale(locale);
    
    DisplayMetrics displayMetrics = resources.getDisplayMetrics();
    resources.updateConfiguration(configuration, displayMetrics);
  }
}

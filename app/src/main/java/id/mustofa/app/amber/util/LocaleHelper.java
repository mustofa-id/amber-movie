package id.mustofa.app.amber.util;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.DisplayMetrics;

import java.util.Locale;

/**
 * @author Habib Mustofa
 * Indonesia on 06/07/19.
 */
public class LocaleHelper {
  
  // TODO: Save configuration to persistent storage
  public static void updateLocale(Context context, String langId) {
    Resources resources = context.getResources();
    
    Locale locale = new Locale(langId);
    Locale.setDefault(locale);
    
    Configuration configuration = new Configuration();
    configuration.setLocale(locale);
    
    DisplayMetrics displayMetrics = resources.getDisplayMetrics();
    resources.updateConfiguration(configuration, displayMetrics);
  }
}

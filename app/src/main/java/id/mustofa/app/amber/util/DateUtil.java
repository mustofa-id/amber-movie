package id.mustofa.app.amber.util;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * @author Habib Mustofa
 * Indonesia on 07/07/19.
 */
public class DateUtil {
  
  private static final String TAG = DateUtil.class.getName();
  
  public static String reformatDate(String source, String newFormat) {
    try {
      String formatFromApi = "yyyy-MM-dd";
      Date parse = new SimpleDateFormat(formatFromApi, Locale.getDefault()).parse(source);
      return new SimpleDateFormat(newFormat, Locale.getDefault()).format(parse);
    } catch (ParseException e) {
      Log.w(TAG, "Fail perform reformatDate: ", e);
      return source;
    }
  }
}

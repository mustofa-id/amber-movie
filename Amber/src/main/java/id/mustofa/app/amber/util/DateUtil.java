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
  private static final String DATE_API_FORMAT = "yyyy-MM-dd";
  
  public static String reformatDate(String source, String newFormat) {
    try {
      Date parse = new SimpleDateFormat(DATE_API_FORMAT, Locale.getDefault()).parse(source);
      return new SimpleDateFormat(newFormat, Locale.getDefault()).format(parse);
    } catch (ParseException e) {
      Log.w(TAG, "Fail perform reformatDate: ", e);
      return source;
    }
  }
  
  public static String format(Date date) {
    SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_API_FORMAT, Locale.getDefault());
    return dateFormat.format(date);
  }
}

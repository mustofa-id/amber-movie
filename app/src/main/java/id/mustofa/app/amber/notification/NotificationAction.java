package id.mustofa.app.amber.notification;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.Calendar;

/**
 * @author Habib Mustofa
 * Indonesia on 18/07/19.
 */
public class NotificationAction {
  
  static final String KEY_EXTRA_TYPE = "KEY_EXTRA_TYPE__";
  static final String TYPE_DAILY_REMINDER = "DAILY_REMINDER_";
  static final String TYPE_RELEASE_TODAY = "RELEASE_TODAY_";
  
  private static final String TAG = NotificationAction.class.getName();
  private static final int[] DAILY_REMINDER_TIME = {18, 45, 0}; // hours, minutes, second
  private static final int[] RELEASE_TODAY_TIME = {18, 50, 0}; // hours, minutes, second
  private static final int DAILY_REMINDER_REQ_CODE = 107;
  private static final int RELEASE_TODAY_REQ_CODE = 108;
  
  private final Context mContext;
  private final AlarmManager mAlarm;
  
  public NotificationAction(@NonNull Context context) {
    this.mContext = context;
    this.mAlarm = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
  }
  
  public void setDailyReminderEnabled(boolean state) {
    if (state) {
      Calendar calendar = Calendar.getInstance();
      calendar.set(Calendar.HOUR_OF_DAY, DAILY_REMINDER_TIME[0]);
      calendar.set(Calendar.MINUTE, DAILY_REMINDER_TIME[1]);
      calendar.set(Calendar.SECOND, DAILY_REMINDER_TIME[2]);
      setAlarm(calendar, DAILY_REMINDER_REQ_CODE, TYPE_DAILY_REMINDER);
    } else {
      cancelAlarm(DAILY_REMINDER_REQ_CODE);
    }
    Log.d(TAG, "setDailyReminderEnabled: " + state);
  }
  
  public void setReleaseTodayEnabled(boolean state) {
    if (state) {
      Calendar calendar = Calendar.getInstance();
      calendar.set(Calendar.HOUR_OF_DAY, RELEASE_TODAY_TIME[0]);
      calendar.set(Calendar.MINUTE, RELEASE_TODAY_TIME[1]);
      calendar.set(Calendar.SECOND, RELEASE_TODAY_TIME[2]);
      setAlarm(calendar, RELEASE_TODAY_REQ_CODE, TYPE_RELEASE_TODAY);
    } else {
      cancelAlarm(RELEASE_TODAY_REQ_CODE);
    }
    Log.d(TAG, "setReleaseTodayEnabled: " + state);
  }
  
  private void setAlarm(Calendar calendar, int reqCode, String type) {
    PendingIntent intent = getPendingIntent(PackageManager.COMPONENT_ENABLED_STATE_ENABLED, reqCode, type);
    mAlarm.setInexactRepeating(AlarmManager.RTC_WAKEUP,
        calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, intent);
  }
  
  private void cancelAlarm(int reqCode) {
    PendingIntent intent = getPendingIntent(PackageManager.COMPONENT_ENABLED_STATE_DISABLED, reqCode, null);
    mAlarm.cancel(intent);
    intent.cancel();
  }
  
  private PendingIntent getPendingIntent(int newState, int reqCode, String type) {
    ComponentName componentName = new ComponentName(mContext, NotificationReceiver.class);
    PackageManager packageManager = mContext.getPackageManager();
    packageManager.setComponentEnabledSetting(componentName, newState, PackageManager.DONT_KILL_APP);
    Intent intent = new Intent(mContext, NotificationReceiver.class);
    if (type != null) intent.putExtra(KEY_EXTRA_TYPE, type);
    return PendingIntent.getBroadcast(mContext, reqCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);
  }
}

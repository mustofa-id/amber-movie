package id.mustofa.app.amber.notification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.preference.PreferenceManager;
import android.util.Log;

import java.util.Objects;

import id.mustofa.app.amber.R;
import id.mustofa.app.amber.util.NotificationBuilder;

/**
 * @author Habib Mustofa
 * Indonesia on 18/07/19.
 */
public class NotificationReceiver extends BroadcastReceiver {
  
  private static final String TAG = NotificationReceiver.class.getName();
  
  @Override
  public void onReceive(Context context, Intent intent) {
    if (context == null) {
      Log.d(TAG, "onReceive: NULL_CONTEXT");
      return;
    }
    
    String action = intent.getAction();
    if (action != null) {
      if (action.equalsIgnoreCase(Intent.ACTION_BOOT_COMPLETED)) {
        Log.d(TAG, "onReceive: BOOT_COMPLETED");
        resetNotificationSetting(context);
        return;
      }
    }
    
    String type = intent.getStringExtra(NotificationAction.KEY_EXTRA_TYPE);
    Log.d(TAG, "onReceive: PUSH_NOTIFICATION " + type);
    if (Objects.equals(type, NotificationAction.TYPE_DAILY_REMINDER)) {
      pushDailyReminderNotification(context);
    } else if (Objects.equals(type, NotificationAction.TYPE_RELEASE_TODAY)) {
      pushReleaseTodayNotification(context);
    }
  }
  
  private void pushDailyReminderNotification(Context context) {
    NotificationBuilder notification = new NotificationBuilder(context)
        .withChannel("CH1", "General")
        .applyDefault();
    
    notification.getBuilder()
        .setContentTitle("Test")
        .setContentText("Testing alarm daily reminder");
    
    notification.push(1234);
  }
  
  private void pushReleaseTodayNotification(Context context) {
    NotificationBuilder notification = new NotificationBuilder(context)
        .withChannel("CH1", "General")
        .applyDefault();
    
    notification.getBuilder()
        .setContentTitle("Test II")
        .setContentText("Testing alarm release today");
    
    notification.push(4321);
  }
  
  private void resetNotificationSetting(Context context) {
    final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
    boolean dailyReminder = prefs.getBoolean(context.getString(R.string.key_prefs_daily_reminder), false);
    boolean releaseToday = prefs.getBoolean(context.getString(R.string.key_prefs_release_today), false);
    NotificationAction notificationAction = new NotificationAction(context);
    notificationAction.setDailyReminderEnabled(dailyReminder);
    notificationAction.setReleaseTodayEnabled(releaseToday);
  }
}

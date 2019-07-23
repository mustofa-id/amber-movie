package id.mustofa.app.amber.util;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import id.mustofa.app.amber.R;

/**
 * @author Habib Mustofa
 * Indonesia on 18/07/19.
 */
public class NotificationBuilder {
  
  private static final String TAG = NotificationBuilder.class.getName();
  private static final long[] VIBRATE_PATTERN = new long[]{0, 200, 50, 200, 50, 800};
  
  private final Context mContext;
  private final NotificationManager mNotificationManager;
  
  private NotificationCompat.Builder mNotificationBuilder;
  private String mChannelId, mChannelName;
  
  public NotificationBuilder(@NonNull Context context) {
    this.mContext = context;
    this.mNotificationManager = (NotificationManager)
        context.getSystemService(Context.NOTIFICATION_SERVICE);
  }
  
  public NotificationBuilder withChannel(String channelId, String channelName) {
    this.mChannelId = channelId;
    this.mChannelName = channelName;
    mNotificationBuilder = new NotificationCompat.Builder(mContext, channelId);
    return this;
  }
  
  public NotificationBuilder applyDefault() {
    Uri tone = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
    mNotificationBuilder
        .setSmallIcon(R.drawable.ic_stat_default_notification)
        .setColor(ContextCompat.getColor(mContext, R.color.colorAccent))
        .setVibrate(VIBRATE_PATTERN)
        .setSound(tone)
        .setLights(Color.YELLOW, 500, 500);
    return this;
  }
  
  public NotificationCompat.Builder getBuilder() {
    return mNotificationBuilder;
  }
  
  public void push(int notifyId) {
    if (mNotificationManager == null) {
      Log.w(TAG, "Can't push notification while NotificationManager is null");
      return;
    }
    
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      NotificationChannel channel = new NotificationChannel(
          mChannelId, mChannelName, NotificationManager.IMPORTANCE_DEFAULT);
      channel.enableVibration(true);
      channel.setVibrationPattern(VIBRATE_PATTERN);
      mNotificationManager.createNotificationChannel(channel);
    }
    
    mNotificationManager.notify(notifyId, mNotificationBuilder.build());
  }
}

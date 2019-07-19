package id.mustofa.app.amber.notification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.NotificationCompat;
import android.support.v7.preference.PreferenceManager;
import android.util.Log;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import id.mustofa.app.amber.R;
import id.mustofa.app.amber.data.MovieRepository;
import id.mustofa.app.amber.data.model.MediaType;
import id.mustofa.app.amber.data.model.Movie;
import id.mustofa.app.amber.util.DateUtil;
import id.mustofa.app.amber.util.Injection;
import id.mustofa.app.amber.util.NotificationBuilder;

import static id.mustofa.app.amber.util.QueryHelper.Params;
import static id.mustofa.app.amber.util.QueryHelper.createQuery;

/**
 * @author Habib Mustofa
 * Indonesia on 18/07/19.
 */
public class NotificationReceiver extends BroadcastReceiver {
  
  private static final String TAG = NotificationReceiver.class.getName();
  private static final String CHANNEL_ID = "CHANNEL_ID001";
  private static final String CHANNEL_NAME = "General";
  
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
      fetchMovieReleaseToday(context);
    }
  }
  
  private void pushDailyReminderNotification(Context context) {
    NotificationBuilder notification = new NotificationBuilder(context)
        .withChannel(CHANNEL_ID, CHANNEL_NAME)
        .applyDefault();
    notification.getBuilder()
        .setContentTitle(context.getString(R.string.title_prefs_daily_reminder))
        .setContentText(context.getString(R.string.desc_daily_reminder_notification_text));
    notification.push(9999);
  }
  
  private void pushReleaseTodayNotification(Context context, List<Movie> movies) {
    int maxNotify = movies.size() - 1;
    int idNotify = 0;
    String groupNotify = "ReleaseToday";
    NotificationCompat.InboxStyle style = new NotificationCompat.InboxStyle()
        .setSummaryText(context.getString(R.string.title_prefs_release_today));
    NotificationBuilder notification;
    for (Movie movie : movies) {
      style.addLine(movie.getTitle());
      if (idNotify < maxNotify) {
        notification = new NotificationBuilder(context)
            .withChannel(CHANNEL_ID, CHANNEL_NAME)
            .applyDefault();
        notification.getBuilder()
            .setContentTitle(movie.getTitle())
            .setContentText(movie.getOverview())
            .setGroup(groupNotify);
        notification.push(idNotify);
      } else {
        notification = new NotificationBuilder(context)
            .withChannel(CHANNEL_ID, CHANNEL_NAME)
            .applyDefault();
        
        // TODO: Extract to string with english type
        final String contentText = String.format("%s new movie release", idNotify);
        style.setBigContentTitle(contentText);
        notification.getBuilder()
            .setContentTitle(movie.getTitle())
            .setContentText(contentText) // support devices running API level < 24
            .setStyle(style)
            .setGroup(groupNotify)
            .setGroupSummary(true);
        notification.push(maxNotify);
      }
      idNotify++;
    }
    
  }
  
  private void fetchMovieReleaseToday(Context context) {
    final MovieRepository repository = Injection.provideMovieRepository(context);
    String today = DateUtil.format(new Date());
    Map<String, String> params = createQuery(
        Params.RELEASE_DATE, today,
        Params.SORT_BY, "primary_release_date.asc"
    );
    repository.findMovies(MediaType.MOVIE, params, (result, error) -> {
      if (error == null && !result.isEmpty()) {
        Iterator<Movie> moviesIt = result.iterator();
        while (moviesIt.hasNext()) {
          Movie movie = moviesIt.next();
          if (!movie.getReleaseDate().equals(today)) {
            moviesIt.remove();
          }
        }
        if (!result.isEmpty()) pushReleaseTodayNotification(context, result);
      }
    });
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

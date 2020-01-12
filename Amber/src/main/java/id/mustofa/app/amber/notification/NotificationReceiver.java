package id.mustofa.app.amber.notification;

import android.app.PendingIntent;
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
import id.mustofa.app.amber.movie.detail.MovieDetailActivity;
import id.mustofa.app.amber.splash.SplashActivity;
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
  
    PendingIntent pendingIntent = getMainPendingIntent(context, 0);
    
    notification.getBuilder()
        .setContentTitle(context.getString(R.string.title_prefs_daily_reminder))
        .setContentText(context.getString(R.string.desc_daily_reminder_notification_text))
        .setContentIntent(pendingIntent)
        .setAutoCancel(true);
    notification.push(9999);
  }
  
  private void pushReleaseTodayNotification(Context context, List<Movie> movies) {
    int maxNotify = movies.size();
    int idNotify = 0;
    String groupNotify = "ReleaseToday";
    NotificationCompat.InboxStyle style = new NotificationCompat.InboxStyle()
        .setSummaryText(context.getString(R.string.title_prefs_release_today));
    NotificationBuilder notification;
    for (Movie movie : movies) {
      style.addLine(movie.getTitle());
      if (idNotify < maxNotify - 1) {
        notification = new NotificationBuilder(context)
            .withChannel(CHANNEL_ID, CHANNEL_NAME)
            .applyDefault();
  
        Intent intent = new Intent(context, MovieDetailActivity.class);
        intent.putExtra(MovieDetailActivity.EXTRA_MOVIE_TYPE, MediaType.MOVIE);
        intent.putExtra(MovieDetailActivity.EXTRA_MOVIE_ITEM, movie);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, idNotify, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        
        notification.getBuilder()
            .setContentTitle(movie.getTitle())
            .setContentText(movie.getOverview())
            .setGroup(groupNotify)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent);
        
        notification.push(idNotify);
      } else {
  
        notification = new NotificationBuilder(context)
            .withChannel(CHANNEL_ID, CHANNEL_NAME)
            .applyDefault();
  
        final String contentText = context.getResources()
            .getQuantityString(R.plurals.msg_release_today_notification_text, maxNotify, maxNotify);
        PendingIntent pendingIntent = getMainPendingIntent(context, idNotify);
        style.setBigContentTitle(contentText);
  
        notification.getBuilder()
            .setContentTitle(movie.getTitle())
            // style#setSummaryText support for devices running API level < 24
            .setContentText(contentText)
            .setStyle(style)
            .setGroup(groupNotify)
            .setGroupSummary(true)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent);
        
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
          // API endpoint only support query release date with gte or lte
          // Here we filter releaseDate only match today
          if (!movie.getReleaseDate().equals(today)) {
            moviesIt.remove();
          }
        }
  
        if (!result.isEmpty()) {
          // Limit list 50 because notification (in default Android System) is limited to 50
          // com.android.server.notification.NotificationManagerService.MAX_PACKAGE_NOTIFICATIONS
          List<Movie> movies = result.size() > 50 ? result.subList(0, 50) : result;
          pushReleaseTodayNotification(context, movies);
        }
      }
    });
  }
  
  private PendingIntent getMainPendingIntent(Context context, int reqCode) {
    Intent intent = new Intent(context, SplashActivity.class);
    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
    return PendingIntent.getActivity(context, reqCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);
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

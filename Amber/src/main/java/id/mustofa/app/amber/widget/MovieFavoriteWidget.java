package id.mustofa.app.amber.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;

import id.mustofa.app.amber.R;
import id.mustofa.app.amber.data.model.MediaType;
import id.mustofa.app.amber.data.model.Movie;
import id.mustofa.app.amber.moviedetail.MovieDetailActivity;

/**
 * @author Habib Mustofa
 * Indonesia on 19/07/19.
 */
public class MovieFavoriteWidget extends AppWidgetProvider {
  
  public static final String ACTION_ITEM_CLICK = "ITEM_CLICK_ACTION_";
  public static final String EXTRA_ITEM = "ITEM_MOVIE_FAVORITE_";
  
  static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {
    Intent intent = new Intent(context, MovieFavoriteWidgetService.class);
    intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
    intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
    RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_movie_favorite);
    views.setRemoteAdapter(R.id.stack_widget_favorite_movies, intent);
    views.setEmptyView(R.id.stack_widget_favorite_movies, R.id.text_widget_favorite_empty_view);
  
    Intent intentAction = new Intent(context, MovieFavoriteWidget.class);
    intentAction.setAction(MovieFavoriteWidget.ACTION_ITEM_CLICK);
    intentAction.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
    PendingIntent actionPendingIntent = PendingIntent.getBroadcast(
        context, 0, intentAction, PendingIntent.FLAG_UPDATE_CURRENT);
    views.setPendingIntentTemplate(R.id.stack_widget_favorite_movies, actionPendingIntent);
    
    appWidgetManager.updateAppWidget(appWidgetId, views);
  }
  
  public static void notifyDataChanged(Context context) {
    AppWidgetManager manager = AppWidgetManager.getInstance(context);
    ComponentName movieFavoriteWidget = new ComponentName(context, MovieFavoriteWidget.class);
    int[] widgetIds = manager.getAppWidgetIds(movieFavoriteWidget);
    manager.notifyAppWidgetViewDataChanged(widgetIds, R.id.stack_widget_favorite_movies);
  }
  
  @Override
  public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
    for (int appWidgetId : appWidgetIds) {
      updateAppWidget(context, appWidgetManager, appWidgetId);
    }
  }
  
  @Override
  public void onReceive(Context context, Intent intent) {
    super.onReceive(context, intent);
    if (intent.getAction() != null) {
      if (intent.getAction().equals(ACTION_ITEM_CLICK)) {
        Movie movie = intent.getParcelableExtra(EXTRA_ITEM);
        Intent movieDetailIntent = new Intent(context, MovieDetailActivity.class);
        movieDetailIntent.putExtra(MovieDetailActivity.EXTRA_MOVIE_ITEM, movie);
        movieDetailIntent.putExtra(MovieDetailActivity.EXTRA_MOVIE_TYPE, MediaType.MOVIE);
        movieDetailIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(movieDetailIntent);
      }
    }
  }
  
  @Override
  public void onEnabled(Context context) {
    notifyDataChanged(context);
  }
  
  @Override
  public void onDisabled(Context context) {}
}


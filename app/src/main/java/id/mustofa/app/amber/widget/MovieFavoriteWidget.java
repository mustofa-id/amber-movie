package id.mustofa.app.amber.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;

import id.mustofa.app.amber.R;

/**
 * @author Habib Mustofa
 * Indonesia on 19/07/19.
 */
public class MovieFavoriteWidget extends AppWidgetProvider {
  
  // TODO: Build this -> MovieFavoriteWidget design and open detail onClicked
  static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {
    Intent intent = new Intent(context, MovieFavoriteWidgetService.class);
    intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
    intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
    RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_movie_favorite);
    views.setRemoteAdapter(R.id.stack_widget_favorite_movies, intent);
    views.setEmptyView(R.id.stack_widget_favorite_movies, R.id.text_widget_favorite_empty_view);
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
  public void onEnabled(Context context) {
    notifyDataChanged(context);
  }
  
  @Override
  public void onDisabled(Context context) {}
}


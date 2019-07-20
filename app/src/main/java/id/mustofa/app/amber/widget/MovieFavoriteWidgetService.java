package id.mustofa.app.amber.widget;

import android.content.Intent;
import android.widget.RemoteViewsService;

/**
 * @author Habib Mustofa
 * Indonesia on 20/07/19.
 */
public class MovieFavoriteWidgetService extends RemoteViewsService {
  @Override
  public RemoteViewsFactory onGetViewFactory(Intent intent) {
    return new MovieFavoriteWidgetFactory(getApplicationContext());
  }
}

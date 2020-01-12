package id.mustofa.app.amber.widget;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.util.ArrayList;
import java.util.List;

import id.mustofa.app.amber.R;
import id.mustofa.app.amber.data.model.MediaType;
import id.mustofa.app.amber.data.model.Movie;
import id.mustofa.app.amber.data.source.local.AppDatabase;
import id.mustofa.app.amber.data.source.local.MovieLocalDao;
import id.mustofa.app.amber.util.ImageLoader;

/**
 * @author Habib Mustofa
 * Indonesia on 20/07/19.
 */
public class MovieFavoriteWidgetFactory implements RemoteViewsService.RemoteViewsFactory {
  
  private final Context mContext;
  private List<DataHolder> mData;
  private MovieLocalDao movieLocalDao;
  
  MovieFavoriteWidgetFactory(@NonNull Context context) {
    this.mContext = context;
  }
  
  @Override
  public void onCreate() {
    mData = new ArrayList<>();
    movieLocalDao = AppDatabase.getInstance(mContext).movieLocalDao();
  }
  
  @Override
  public void onDataSetChanged() {
    final String type = MediaType.MOVIE.getValue();
    final List<Movie> favorites = movieLocalDao.findFavoriteByType(type);
    mData.clear();
    for (Movie movie : favorites) {
      Bitmap bitmap = ImageLoader.getBitmap(mContext, movie.getPosterPath());
      mData.add(new DataHolder(movie, bitmap));
    }
  }
  
  @Override
  public int getCount() {
    return mData.size();
  }
  
  @Override
  public RemoteViews getViewAt(int position) {
    RemoteViews views = new RemoteViews(mContext.getPackageName(), R.layout.item_widget_movie_favorite);
    // Glide AppWidgetTarget doesn't support multiple images :(
    // AppWidgetTarget imageTarget = new AppWidgetTarget(mContext, R.id.imageView, views, mAppWidgetId);
    // MovieFavorite movie = mMovieFavorites.get(position);
    // ImageLoader.load(mContext, movie.getPosterPath(), imageTarget);
    if (!mData.isEmpty()) {
      DataHolder data = mData.get(position);
      views.setImageViewBitmap(R.id.img_item_widget_movie_favorite_poster, data.poster);
      views.setImageViewBitmap(R.id.img_item_widget_movie_favorite_backdrop, data.poster);
      views.setTextViewText(R.id.text_item_widget_movie_favorite_title, data.movieFavorite.getTitle());
    
      Intent intent = new Intent();
      intent.putExtra(MovieFavoriteWidget.EXTRA_ITEM, data.movieFavorite);
      views.setOnClickFillInIntent(R.id.parent_item_widget_movie_favorite, intent);
    }
    return views;
  }
  
  @Override
  public RemoteViews getLoadingView() {
    return null;
  }
  
  @Override
  public int getViewTypeCount() {
    return 1;
  }
  
  @Override
  public long getItemId(int position) {
    if (mData.isEmpty()) return position;
    Movie favorite = mData.get(position).movieFavorite;
    return favorite.getId();
  }
  
  @Override
  public boolean hasStableIds() {
    return true;
  }
  
  @Override
  public void onDestroy() {
    mData.clear();
    movieLocalDao = null;
  }
  
  // Helper class for hold bitmap that fetched synchronously
  // on dataSetChanged will make getViewAt load fast
  private class DataHolder {
  
    private Movie movieFavorite;
    private Bitmap poster;
  
    private DataHolder(Movie movieFavorite, Bitmap poster) {
      this.movieFavorite = movieFavorite;
      this.poster = poster;
    }
  }
}

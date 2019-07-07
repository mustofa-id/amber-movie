package id.mustofa.app.amber.util;

import android.content.Context;
import android.support.v4.widget.CircularProgressDrawable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import id.mustofa.app.amber.R;

/**
 * @author Habib Mustofa
 * Indonesia on 06/07/19.
 */
public class ImageLoader {
  
  private static final String BASE_IMAGE_URL = "https://image.tmdb.org/t/p/%s%s";
  private static final String IMAGE_SIZE = "w185";
  
  public static void load(Context context, String path, ImageView target) {
    RequestOptions options = new RequestOptions()
        .diskCacheStrategy(DiskCacheStrategy.ALL)
        .placeholder(placeholder(context))
        .error(R.drawable.ic_warning_black_24dp);
    
    Glide.with(context)
        .asBitmap()
        .load(String.format(BASE_IMAGE_URL, IMAGE_SIZE, path))
        .apply(options)
        .into(target);
  }
  
  private static CircularProgressDrawable placeholder(Context context) {
    CircularProgressDrawable circular = new CircularProgressDrawable(context);
    circular.setStrokeWidth(5);
    circular.setCenterRadius(45);
    circular.start();
    return circular;
  }
}

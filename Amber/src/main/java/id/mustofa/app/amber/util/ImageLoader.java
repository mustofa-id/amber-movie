package id.mustofa.app.amber.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.widget.CircularProgressDrawable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.util.concurrent.ExecutionException;

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
        .error(R.drawable.img_no_image_available);
    
    Glide.with(context)
        .asBitmap()
        .load(getUrl(path))
        .apply(options)
        .into(target);
  }
  
  public static Bitmap getBitmap(Context context, String path) {
    RequestBuilder<Bitmap> builder = Glide.with(context)
        .asBitmap()
        .diskCacheStrategy(DiskCacheStrategy.ALL)
        .load(getUrl(path));
    
    try {
      // Synchronously fetch image
      return builder.submit().get();
    } catch (ExecutionException | InterruptedException e) {
      return BitmapFactory.decodeResource(context.getResources(), R.drawable.img_no_image_available);
    }
  }
  
  private static String getUrl(String path) {
    return String.format(BASE_IMAGE_URL, IMAGE_SIZE, path);
  }
  
  private static CircularProgressDrawable placeholder(Context context) {
    CircularProgressDrawable circular = new CircularProgressDrawable(context);
    circular.setStrokeWidth(5);
    circular.setCenterRadius(45);
    circular.start();
    return circular;
  }
}

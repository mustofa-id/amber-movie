package id.mustofa.app.amber.util;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.module.AppGlideModule;

/**
 * @author Habib Mustofa
 * Indonesia on 20/07/19.
 */
@GlideModule
public class ImageLoaderModule extends AppGlideModule {
  
  @Override
  public void applyOptions(@NonNull Context context, @NonNull GlideBuilder builder) {
    super.applyOptions(context, builder);
    builder.setLogLevel(Log.ERROR);
  }
}

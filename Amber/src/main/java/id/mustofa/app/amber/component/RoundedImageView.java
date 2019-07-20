package id.mustofa.app.amber.component;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;

import id.mustofa.app.amber.R;

/**
 * @author Habib Mustofa
 * Indonesia on 06/07/19.
 */
public class RoundedImageView extends AppCompatImageView {
  
  private float mRadius;
  
  public RoundedImageView(Context context) {
    super(context);
    init();
  }
  
  public RoundedImageView(Context context, AttributeSet attrs) {
    super(context, attrs);
    init();
  }
  
  public RoundedImageView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init();
  }
  
  private void init() {
    Resources resources = getResources();
    float px = resources.getDimension(R.dimen.card_item_movie_radius);
    DisplayMetrics displayMetrics = resources.getDisplayMetrics();
    mRadius = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, px, displayMetrics);
  }

  @Override
  public void setImageDrawable(@Nullable Drawable drawable) {
    if (drawable != null) {
      if (drawable instanceof BitmapDrawable) {
        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
        RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(getResources(), bitmap);
        roundedBitmapDrawable.setCornerRadius(mRadius);
        super.setImageDrawable(roundedBitmapDrawable);
        return;
      }
    }
    super.setImageDrawable(drawable);
  }
}

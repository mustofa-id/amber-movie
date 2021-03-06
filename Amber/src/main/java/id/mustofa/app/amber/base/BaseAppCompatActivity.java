package id.mustofa.app.amber.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import id.mustofa.app.amber.util.LocaleHelper;

/**
 * @author Habib Mustofa
 * Indonesia on 08/07/19.
 *
 * This base activity trigger on createQuery
 */
public abstract class BaseAppCompatActivity extends AppCompatActivity {
  
  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    LocaleHelper.updateLocale(this);
    super.onCreate(savedInstanceState);
  }
}

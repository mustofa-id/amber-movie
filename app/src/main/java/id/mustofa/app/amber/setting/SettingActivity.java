package id.mustofa.app.amber.setting;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;

import java.util.Objects;

import id.mustofa.app.amber.R;
import id.mustofa.app.amber.movie.MovieActivity;
import id.mustofa.app.amber.splash.SplashActivity;
import id.mustofa.app.amber.util.LocaleHelper;

/**
 * @author Habib Mustofa
 * Indonesia on 06/07/19.
 */
public class SettingActivity extends AppCompatActivity {
  
  
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_setting);
    Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
  }
  
  @Override
  public void onBackPressed() {
    startActivity(new Intent(this, MovieActivity.class));
    finish();
  }
  
  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    if (item.getItemId() == android.R.id.home) onBackPressed();
    return super.onOptionsItemSelected(item);
  }
  
  public void onChangeLanguage(View view) {
    PopupMenu popup = new PopupMenu(this, view, Gravity.CENTER);
    Menu menu = popup.getMenu();
    menu.add(0, 0, 0, R.string.title_lang_english);
    menu.add(0, 1, 1, R.string.title_lang_indonesia);
    popup.setOnMenuItemClickListener(item -> {
      String langId = item.getItemId() == 0 ? "en" : "in";
      LocaleHelper.updateLocale(this, langId);
      return restartApp();
    });
    popup.show();
  }
  
  private boolean restartApp() {
    finish();
    startActivity(new Intent(this, SplashActivity.class));
    return true;
  }
}

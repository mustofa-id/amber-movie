package id.mustofa.app.amber.splash;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import id.mustofa.app.amber.main.MainActivity;

/**
 * @author Habib Mustofa
 * Indonesia on 06/07/19.
 */
public class SplashActivity extends AppCompatActivity {
  
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    new Handler().postDelayed(this::startMovieActivity, 800); // Fake loading in 0.8s
  }
  
  private void startMovieActivity() {
    startActivity(new Intent(this, MainActivity.class));
    finish();
  }
}

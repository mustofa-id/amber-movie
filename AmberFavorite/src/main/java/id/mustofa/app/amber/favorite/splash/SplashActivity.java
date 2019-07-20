package id.mustofa.app.amber.favorite.splash;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import id.mustofa.app.amber.favorite.main.MainActivity;

public class SplashActivity extends AppCompatActivity {
  
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    startMainActivity();
  }
  
  private void startMainActivity() {
    startActivity(new Intent(this, MainActivity.class));
    finish();
  }
}

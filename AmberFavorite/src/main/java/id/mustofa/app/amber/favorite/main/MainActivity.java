package id.mustofa.app.amber.favorite.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.Iterator;
import java.util.List;

import id.mustofa.app.amber.favorite.R;
import id.mustofa.app.amber.favorite.data.DataAccessAsync;
import id.mustofa.app.amber.favorite.data.model.Movie;
import id.mustofa.app.amber.favorite.detail.DetailActivity;

public class MainActivity extends AppCompatActivity
    implements DataAccessAsync.Callback<List<Movie>>, MainItemNavigator {
  
  private static final String KEY_FILTER_TYPE = "KEY_FILTER_TYPE";
  
  private MainGridAdapter mAdapter;
  private MainFilterType mType = MainFilterType.ALL;
  private TextView mTextEmpty;
  
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    setupRecyclerView();
  
    if (savedInstanceState != null) {
      mType = (MainFilterType) savedInstanceState.getSerializable(KEY_FILTER_TYPE);
    }
  }
  
  @Override
  protected void onResume() {
    super.onResume();
    getAllMovies();
  }
  
  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.menu_main_activity, menu);
    return super.onCreateOptionsMenu(menu);
  }
  
  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    int id = item.getItemId();
    if (id == R.id.act_filter_all) {
      mType = MainFilterType.ALL;
    } else if (id == R.id.act_filter_movie) {
      mType = MainFilterType.MOVIE;
    } else if (id == R.id.act_filter_tv) {
      mType = MainFilterType.TV;
    }
    getAllMovies();
    return super.onOptionsItemSelected(item);
  }
  
  private void getAllMovies() {
    new DataAccessAsync.GetMovies(getContentResolver(), this).execute();
  }
  
  private void setupRecyclerView() {
    mTextEmpty = findViewById(R.id.text_main_empty_data);
    int spanCount = getResources().getInteger(R.integer.grid_span_count);
    RecyclerView recyclerView = findViewById(R.id.rv_main_movies);
    recyclerView.setHasFixedSize(true);
    mAdapter = new MainGridAdapter(this);
    recyclerView.setLayoutManager(new GridLayoutManager(this, spanCount));
    recyclerView.setAdapter(mAdapter);
  }
  
  @Override
  public void onResult(List<Movie> movies) {
    filterMovies(movies);
    mAdapter.populateData(movies);
    mTextEmpty.setVisibility(movies.isEmpty() ? View.VISIBLE : View.GONE);
  }
  
  @Override
  public void onItemClick(Movie movie) {
    Intent intent = new Intent(this, DetailActivity.class);
    intent.setData(movie.getUri());
    startActivity(intent);
  }
  
  private void filterMovies(List<Movie> movies) {
    final String defaultTitle = getString(R.string.title_main_activity);
    final Iterator<Movie> iterator = movies.iterator();
    switch (mType) {
      case ALL:
        setTitle(defaultTitle);
        break;
      case MOVIE:
        while (iterator.hasNext()) {
          Movie movie = iterator.next();
          if (movie.getType().equals("tv")) {
            iterator.remove();
          }
        }
        setTitle(String.format("%s (%s)", defaultTitle, getString(R.string.action_movie)));
        break;
      case TV:
        while (iterator.hasNext()) {
          Movie movie = iterator.next();
          if (movie.getType().equals("movie")) {
            iterator.remove();
          }
        }
        setTitle(String.format("%s (%s)", defaultTitle, getString(R.string.action_tv_show)));
        break;
    }
  }
  
  @Override
  protected void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    outState.putSerializable(KEY_FILTER_TYPE, mType);
  }
}

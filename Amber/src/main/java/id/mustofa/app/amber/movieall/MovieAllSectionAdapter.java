package id.mustofa.app.amber.movieall;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import id.mustofa.app.amber.R;

/**
 * @author Habib Mustofa
 * Indonesia on 24/07/19.
 */
class MovieAllSectionAdapter extends RecyclerView.Adapter<MovieAllSectionAdapter.MovieAllSectionHolder> {
  
  private final MovieAllItemNavigator mItemNavigator;
  private final List<MovieAllSectionItem> mSectionItems;
  private final RecyclerView.RecycledViewPool mViewPool;
  
  MovieAllSectionAdapter(@NonNull MovieAllItemNavigator mItemNavigator) {
    this.mItemNavigator = mItemNavigator;
    this.mSectionItems = new ArrayList<>();
    this.mViewPool = new RecyclerView.RecycledViewPool();
  }
  
  void addSectionItem(MovieAllSectionItem item) {
    mSectionItems.add(item);
  }
  
  @NonNull
  @Override
  public MovieAllSectionHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
    View view = LayoutInflater.from(viewGroup.getContext())
        .inflate(R.layout.section_movie_all, viewGroup, false);
    return new MovieAllSectionHolder(view);
  }
  
  @Override
  public void onBindViewHolder(@NonNull MovieAllSectionHolder sectionHolder, int i) {
    sectionHolder.setupNestedRecyclerView(mViewPool);
    sectionHolder.setItem(mSectionItems.get(i));
  }
  
  @Override
  public int getItemCount() {
    return mSectionItems.size();
  }
  
  class MovieAllSectionHolder extends RecyclerView.ViewHolder {
    
    private TextView title, emptyMessage;
    private Button more;
    private RecyclerView recyclerView;
    private ProgressBar loading;
    
    MovieAllSectionHolder(@NonNull View view) {
      super(view);
      title = view.findViewById(R.id.text_section_title);
      emptyMessage = view.findViewById(R.id.text_section_empty_message);
      more = view.findViewById(R.id.btn_section_more);
      recyclerView = view.findViewById(R.id.rv_section_movies);
      loading = view.findViewById(R.id.pb_section_loading);
    }
    
    private void setupNestedRecyclerView(@NonNull RecyclerView.RecycledViewPool viewPool) {
      recyclerView.setHasFixedSize(true);
      recyclerView.setLayoutManager(new LinearLayoutManager(
          itemView.getContext(), LinearLayoutManager.HORIZONTAL, false));
      recyclerView.setRecycledViewPool(viewPool);
      
    }
    
    private void setItem(MovieAllSectionItem item) {
      setLoading(true);
      showEmptyMessage(false);
      title.setText(item.getTitle());
      final MovieAllListAdapter adapter = item.getAdapter();
      adapter.setItemNavigator(mItemNavigator);
      recyclerView.setAdapter(adapter);
      more.setOnClickListener(v -> mItemNavigator.openSectionDetail(item));
      adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
        @Override
        public void onChanged() {
          super.onChanged();
          setLoading(false);
          showEmptyMessage(adapter.isEmpty());
        }
      });
    }
    
    private void setLoading(boolean active) {
      loading.setVisibility(active ? View.VISIBLE : View.GONE);
    }
    
    private void showEmptyMessage(boolean show) {
      emptyMessage.setVisibility(show ? View.VISIBLE : View.GONE);
    }
  }
}

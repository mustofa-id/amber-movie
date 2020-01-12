package id.mustofa.app.amber.util;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

/**
 * @author Habib Mustofa
 * Indonesia on 25/07/19.
 */
public class InfiniteScroll {
  
  private final RecyclerView recyclerView;
  private LinearLayoutManager layoutManager;
  
  private InfiniteScroll(RecyclerView recyclerView) {
    this.recyclerView = recyclerView;
  }
  
  public static InfiniteScroll with(@NonNull RecyclerView recyclerView) {
    return new InfiniteScroll(recyclerView);
  }
  
  public InfiniteScroll and(@NonNull LinearLayoutManager layoutManager) {
    this.layoutManager = layoutManager;
    return this;
  }
  
  public void listen(@NonNull ScrollListener listener) {
    recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
      @Override
      public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        if (dy > 0) {
          int visibleItemCount = layoutManager.getChildCount();
          int totalItemCount = layoutManager.getItemCount();
          int pastVisibleItems = layoutManager.findFirstVisibleItemPosition();
          if ((visibleItemCount + pastVisibleItems) >= totalItemCount) {
            listener.onLoadMore();
          }
        }
      }
    });
  }
  
  public interface ScrollListener {
    
    void onLoadMore();
  }
}

package id.mustofa.app.amber.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author Habib Mustofa
 * Indonesia on 06/07/19.
 */
public class MovieWrapper {
  
  @Expose
  private int page;
  
  @Expose
  @SerializedName("total_results")
  private int totalResults;
  
  @Expose
  @SerializedName("total_pages")
  private int totalPages;
  
  @Expose
  private List<Movie> results;
  
  public int getPage() {
    return page;
  }
  
  public int getTotalResults() {
    return totalResults;
  }
  
  public int getTotalPages() {
    return totalPages;
  }
  
  public List<Movie> getResults() {
    return results;
  }
}

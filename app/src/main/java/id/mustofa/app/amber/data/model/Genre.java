package id.mustofa.app.amber.data.model;

import java.util.List;


/**
 * @author Habib Mustofa
 * Indonesia on 05/07/19.
 */
public class Genre {
  
  private long id;
  private String name;
  
  public long getId() {
    return id;
  }
  
  public String getName() {
    return name;
  }
  
  public static class Wrapper {
    public List<Genre> genres;
  }
}

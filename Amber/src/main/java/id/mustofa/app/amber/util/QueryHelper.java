package id.mustofa.app.amber.util;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @author Habib Mustofa
 * Indonesia on 19/07/19.
 */
public class QueryHelper {
  
  public static Map<String, String> createQuery(String... params) {
    final Map<String, String> paramMap = new HashMap<>();
    if (params.length == 0 || params.length % 2 != 0) {
      return paramMap;
    }
    
    Iterator<String> iterator = Arrays.asList(params).iterator();
    while (iterator.hasNext()) {
      paramMap.put(iterator.next(), iterator.next());
    }
    
    return Collections.unmodifiableMap(paramMap);
  }
  
  public interface Params {
    String QUERY = "query";
    String PAGE = "page";
    String INCLUDE_ADULT = "include_adult";
    String RELEASE_DATE = "primary_release_date.gte";
    String SORT_BY = "sort_by";
  }
}

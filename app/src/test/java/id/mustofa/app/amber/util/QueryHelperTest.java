package id.mustofa.app.amber.util;

import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author Habib Mustofa
 * Indonesia on 19/07/19.
 */
public class QueryHelperTest {
  
  @Test
  public void createParamsOk() {
    Map<String, String> params = QueryHelper.createQuery("id", "123", "name", "habib");
    System.out.println(params);
    assertFalse(params.isEmpty());
  }
  
  @Test
  public void createParamsFail() {
    Map<String, String> params = QueryHelper.createQuery("id", "123", "name");
    System.out.println(params);
    assertTrue(params.isEmpty());
  }
}
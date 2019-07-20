package id.mustofa.app.amber.util;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * @author Habib Mustofa
 * Indonesia on 13/07/19.
 */
public class DataConverterTest {
  
  private DataConverter converter = new DataConverter();
  
  @Test
  public void toListLong() {
    String json = "[12,22,33,43]";
    List<Long> longs = converter.toListLong(json);
    assertEquals(longs.get(0).getClass(), Long.class);
    System.out.print("toListLong: ");
    System.out.println(longs);
  }
  
  @Test
  public void fromListLong() {
    List<Long> longs = Arrays.asList(12L, 22L, 33L, 43L);
    String json = converter.fromListLong(longs);
    System.out.print("fromListLong: ");
    System.out.println(json);
    assertNotNull(json);
  }
}
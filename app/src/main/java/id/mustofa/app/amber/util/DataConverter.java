package id.mustofa.app.amber.util;

import android.arch.persistence.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

/**
 * @author Habib Mustofa
 * Indonesia on 11/07/19.
 */
public class DataConverter {
  
  @TypeConverter
  public List<Long> toListLong(String value) {
    return fromJson(value);
  }
  
  @TypeConverter
  public String fromListLong(List<Long> values) {
    return toJson(values);
  }
  
  @TypeConverter
  public List<String> toListString(String value) {
    return fromJson(value);
  }
  
  @TypeConverter
  public String fromListString(List<String> values) {
    return toJson(values);
  }
  
  private <T> String toJson(T value) {
    if (value == null) return null;
    Type type = new TypeToken<T>() {}.getType();
    return new Gson().toJson(value, type);
  }
  
  private <T> T fromJson(String value) {
    if (value == null) return null;
    Type type = new TypeToken<T>() {}.getType();
    return new Gson().fromJson(value, type);
  }
}

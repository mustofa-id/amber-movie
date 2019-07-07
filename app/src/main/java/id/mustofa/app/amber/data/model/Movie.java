package id.mustofa.app.amber.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Habib Mustofa
 * Indonesia on 05/07/19.
 *
 * SerializedName prioritize to the movie. That mean value is assign
 * for movie field and alternate is tv show field from JSON schema.
 */
@SuppressWarnings("unused")
public class Movie implements Parcelable {
  
  @Expose
  private long id;
  
  @Expose
  @SerializedName(value = "title", alternate = {"name"})
  private String title;
  
  @Expose
  @SerializedName(value = "original_title", alternate = {"original_name"})
  private String originalTitle;
  
  @Expose
  @SerializedName("poster_path")
  private String posterPath;
  
  @Expose
  @SerializedName("backdrop_path")
  private String backdropPath;
  
  @Expose
  @SerializedName(value = "release_date", alternate = {"first_air_date"})
  private String releaseDate;
  
  @Expose
  private float popularity;
  
  @Expose
  @SerializedName("vote_count")
  private int voteCount;
  
  @Expose
  @SerializedName("vote_average")
  private float voteAverage;
  
  @Expose
  @SerializedName("overview")
  private String overview;
  
  @Expose
  @SerializedName("original_language")
  private String originalLanguage;
  
  // Only available in TV show
  @Expose
  @SerializedName("origin_country")
  private List<String> originCountry;
  
  @Expose
  @SerializedName("genre_ids")
  private List<Long> genreIds = new ArrayList<>();
  
  // Only available in Movie
  @Expose
  private boolean adult;
  
  private Movie(Parcel in) {
    id = in.readLong();
    title = in.readString();
    originalTitle = in.readString();
    posterPath = in.readString();
    backdropPath = in.readString();
    releaseDate = in.readString();
    popularity = in.readFloat();
    voteCount = in.readInt();
    voteAverage = in.readFloat();
    overview = in.readString();
    originalLanguage = in.readString();
    originCountry = in.createStringArrayList();
    in.readList(genreIds, Long.class.getClassLoader());
    adult = in.readByte() != 0;
  }
  
  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeLong(id);
    dest.writeString(title);
    dest.writeString(originalTitle);
    dest.writeString(posterPath);
    dest.writeString(backdropPath);
    dest.writeString(releaseDate);
    dest.writeFloat(popularity);
    dest.writeInt(voteCount);
    dest.writeFloat(voteAverage);
    dest.writeString(overview);
    dest.writeString(originalLanguage);
    dest.writeStringList(originCountry);
    dest.writeList(genreIds);
    dest.writeByte((byte) (adult ? 1 : 0));
  }
  
  @Override
  public int describeContents() {
    return 0;
  }
  
  public static final Creator<Movie> CREATOR = new Creator<Movie>() {
    @Override
    public Movie createFromParcel(Parcel in) {
      return new Movie(in);
    }
    
    @Override
    public Movie[] newArray(int size) {
      return new Movie[size];
    }
  };
  
  public long getId() {
    return id;
  }
  
  public String getTitle() {
    return title;
  }
  
  public String getOriginalTitle() {
    return originalTitle;
  }
  
  public String getPosterPath() {
    return posterPath;
  }
  
  public String getBackdropPath() {
    return backdropPath;
  }
  
  public String getReleaseDate() {
    return releaseDate;
  }
  
  public float getPopularity() {
    return popularity;
  }
  
  public int getVoteCount() {
    return voteCount;
  }
  
  public float getVoteAverage() {
    return voteAverage;
  }
  
  public String getOverview() {
    return overview;
  }
  
  public String getOriginalLanguage() {
    return originalLanguage;
  }
  
  public List<String> getOriginCountry() {
    return originCountry;
  }
  
  public List<Long> getGenreIds() {
    return genreIds;
  }
  
  public boolean isAdult() {
    return adult;
  }
}

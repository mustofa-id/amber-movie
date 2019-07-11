package id.mustofa.app.amber.data.model;

import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Habib Mustofa
 * Indonesia on 05/07/19.
 * <p>
 * SerializedName prioritize to the movie. That mean value is assign
 * for movie field and alternate is tv show field from JSON schema.
 */
@SuppressWarnings("unused")
public class Movie implements Parcelable {
  
  @PrimaryKey
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
  
  // Room required no-args computer
  public Movie() {}
  
  @Ignore
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
  
  @Ignore
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
  
  @Ignore
  @Override
  public int describeContents() {
    return 0;
  }
  
  @Ignore
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
  
  public void setId(long id) {
    this.id = id;
  }
  
  public String getTitle() {
    return title;
  }
  
  public void setTitle(String title) {
    this.title = title;
  }
  
  public String getOriginalTitle() {
    return originalTitle;
  }
  
  public void setOriginalTitle(String originalTitle) {
    this.originalTitle = originalTitle;
  }
  
  public String getPosterPath() {
    return posterPath;
  }
  
  public void setPosterPath(String posterPath) {
    this.posterPath = posterPath;
  }
  
  public String getBackdropPath() {
    return backdropPath;
  }
  
  public void setBackdropPath(String backdropPath) {
    this.backdropPath = backdropPath;
  }
  
  public String getReleaseDate() {
    return releaseDate;
  }
  
  public void setReleaseDate(String releaseDate) {
    this.releaseDate = releaseDate;
  }
  
  public float getPopularity() {
    return popularity;
  }
  
  public void setPopularity(float popularity) {
    this.popularity = popularity;
  }
  
  public int getVoteCount() {
    return voteCount;
  }
  
  public void setVoteCount(int voteCount) {
    this.voteCount = voteCount;
  }
  
  public float getVoteAverage() {
    return voteAverage;
  }
  
  public void setVoteAverage(float voteAverage) {
    this.voteAverage = voteAverage;
  }
  
  public String getOverview() {
    return overview;
  }
  
  public void setOverview(String overview) {
    this.overview = overview;
  }
  
  public String getOriginalLanguage() {
    return originalLanguage;
  }
  
  public void setOriginalLanguage(String originalLanguage) {
    this.originalLanguage = originalLanguage;
  }
  
  public List<String> getOriginCountry() {
    return originCountry;
  }
  
  public void setOriginCountry(List<String> originCountry) {
    this.originCountry = originCountry;
  }
  
  public List<Long> getGenreIds() {
    return genreIds;
  }
  
  public void setGenreIds(List<Long> genreIds) {
    this.genreIds = genreIds;
  }
  
  public boolean isAdult() {
    return adult;
  }
  
  public void setAdult(boolean adult) {
    this.adult = adult;
  }
}

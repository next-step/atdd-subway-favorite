package nextstep.subway.applicaion.dto;

import java.time.LocalDateTime;
import nextstep.subway.domain.Favorite;
import nextstep.subway.domain.Station;

public class FavoriteResponse {

  private Long id;
  private FavoriteStationResponse source;
  private FavoriteStationResponse target;

  protected FavoriteResponse() {}

  public FavoriteResponse(Long id, FavoriteStationResponse source, FavoriteStationResponse target) {
    this.id = id;
    this.source = source;
    this.target = target;
  }

  public static FavoriteResponse of(Favorite favorite) {
    FavoriteStationResponse sourceStation = FavoriteStationResponse.of(favorite.getSourceStation());
    FavoriteStationResponse targetStation = FavoriteStationResponse.of(favorite.getTargetStation());

    return new FavoriteResponse(favorite.getId(), sourceStation, targetStation);
  }

  public Long getId() {
    return id;
  }

  public FavoriteStationResponse getSource() {
    return source;
  }

  public FavoriteStationResponse getTarget() {
    return target;
  }

  public static class FavoriteStationResponse {
    private Long id;
    private String name;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;

    protected FavoriteStationResponse() {}

    public FavoriteStationResponse(Long id, String name, LocalDateTime createdDate, LocalDateTime modifiedDate) {
      this.id = id;
      this.name = name;
      this.createdDate = createdDate;
      this.modifiedDate = modifiedDate;
    }

    public static FavoriteStationResponse of(Station station) {
      return new FavoriteStationResponse(station.getId(), station.getName(), station.getCreatedDate(), station.getModifiedDate());
    }

    public Long getId() {
      return id;
    }

    public String getName() {
      return name;
    }

    public LocalDateTime getCreatedDate() {
      return createdDate;
    }

    public LocalDateTime getModifiedDate() {
      return modifiedDate;
    }
  }
}

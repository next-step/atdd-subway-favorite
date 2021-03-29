package nextstep.subway.favorite.dto;

import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;

public class FavoriteResponse {
  private final long id;
  private final StationResponse source;
  private final StationResponse target;

  private FavoriteResponse(long id, StationResponse source, StationResponse target) {
    this.id = id;
    this.source = source;
    this.target = target;
  }

  public static FavoriteResponse of(long id, StationResponse source, StationResponse target) {
    return new FavoriteResponse(id, source, target);
  }

  public static FavoriteResponse of(Favorite favorite, Station source, Station target) {
    return of(favorite.getId(), StationResponse.of(source), StationResponse.of(target));
  }

  public long getId() {
    return id;
  }

  public StationResponse getSource() {
    return source;
  }

  public StationResponse getTarget() {
    return target;
  }
}
package nextstep.favorite.application.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import nextstep.subway.station.application.dto.StationResponse;
import nextstep.subway.station.domain.Station;

@Getter
@EqualsAndHashCode
public class FavoriteResponse {
  private final Long id;
  private final StationResponse source;
  private final StationResponse target;

  public FavoriteResponse(Long id, StationResponse source, StationResponse target) {
    this.id = id;
    this.source = source;
    this.target = target;
  }

  public static FavoriteResponse of(Long id, Station source, Station target) {
    return new FavoriteResponse(id, StationResponse.from(source), StationResponse.from(target));
  }
}

package nextstep.subway.station.application.dto;

import java.util.List;
import java.util.stream.Collectors;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import nextstep.subway.station.domain.Station;

@Getter
@EqualsAndHashCode
@ToString
public class StationResponse {
  private final Long id;
  private final String name;

  public StationResponse(Long id, String name) {
    this.id = id;
    this.name = name;
  }

  public static StationResponse from(Station station) {
    return new StationResponse(station.getId(), station.getName());
  }

  public static List<StationResponse> listOf(List<Station> stations) {
    return stations.stream().map(StationResponse::from).collect(Collectors.toList());
  }
}

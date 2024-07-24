package nextstep.subway.line.application.dto;

import java.util.List;
import java.util.stream.Collectors;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import nextstep.subway.line.domain.Line;
import nextstep.subway.station.application.dto.StationResponse;

@Getter
@EqualsAndHashCode
@ToString
public class LineResponse {
  private final Long id;
  private final String name;
  private final String color;
  private final List<StationResponse> stations;

  public LineResponse(Long id, String name, String color, List<StationResponse> stations) {
    this.id = id;
    this.name = name;
    this.color = color;
    this.stations = stations;
  }

  public static LineResponse from(Line line) {
    return new LineResponse(
        line.getId(), line.getName(), line.getColor(), StationResponse.listOf(line.getStations()));
  }

  public static List<LineResponse> listOf(List<Line> lines) {
    return lines.stream().map(LineResponse::from).collect(Collectors.toList());
  }
}

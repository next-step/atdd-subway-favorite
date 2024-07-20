package nextstep.subway.line.application.dto;

import lombok.Getter;
import nextstep.subway.line.domain.Line;

@Getter
public class CreateLineRequest {
  private final String name;
  private final String color;
  private final Long upStationId;
  private final Long downStationId;
  private final Integer distance;

  public CreateLineRequest(
      String name, String color, Long upStationId, Long downStationId, Integer distance) {
    this.name = name;
    this.color = color;
    this.upStationId = upStationId;
    this.downStationId = downStationId;
    this.distance = distance;
  }

  public Line toLine() {
    return new Line(name, color);
  }

  public AppendLineSectionRequest toAddLineSection() {
    return new AppendLineSectionRequest(upStationId, downStationId, distance);
  }
}

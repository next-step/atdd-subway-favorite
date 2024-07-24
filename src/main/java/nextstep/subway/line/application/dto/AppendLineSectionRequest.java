package nextstep.subway.line.application.dto;

import lombok.Getter;

@Getter
public class AppendLineSectionRequest {
  private final Long upStationId;
  private final Long downStationId;
  private final Integer distance;

  public AppendLineSectionRequest(Long upStationId, Long downStationId, Integer distance) {
    this.upStationId = upStationId;
    this.downStationId = downStationId;
    this.distance = distance;
  }
}

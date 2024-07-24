package nextstep.subway.line.application.dto;

import lombok.Getter;

@Getter
public class UpdateLineRequest {
  private final String name;
  private final String color;

  public UpdateLineRequest(String name, String color) {
    this.name = name;
    this.color = color;
  }
}

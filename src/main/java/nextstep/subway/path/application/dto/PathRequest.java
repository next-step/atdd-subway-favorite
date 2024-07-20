package nextstep.subway.path.application.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode(of = {"source", "target"})
public class PathRequest {
  private final Long source;
  private final Long target;

  public PathRequest(Long source, Long target) {
    this.source = source;
    this.target = target;
  }

  public static PathRequest of(Long source, Long target) {
    return new PathRequest(source, target);
  }
}

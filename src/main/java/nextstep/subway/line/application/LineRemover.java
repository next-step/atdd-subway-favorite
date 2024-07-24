package nextstep.subway.line.application;

import lombok.RequiredArgsConstructor;
import nextstep.subway.line.domain.LineRepository;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LineRemover {
  private final LineRepository lineRepository;

  public void remove(Long id) {
    lineRepository.deleteById(id);
  }
}

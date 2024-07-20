package nextstep.subway.line.application;

import java.util.List;
import lombok.RequiredArgsConstructor;
import nextstep.subway.line.application.dto.CreateLineRequest;
import nextstep.subway.line.application.dto.UpdateLineRequest;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineSection;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LineService {
  private final LineAppender lineAppender;
  private final LineReader lineReader;
  private final LineModifier lineModifier;
  private final LineRemover lineRemover;
  private final LineSectionMapper lineSectionMapper;

  @Transactional
  public Line saveLine(CreateLineRequest request) {
    Line line = lineAppender.append(request.toLine());
    LineSection lineSection = lineSectionMapper.map(request.toAddLineSection());
    line.addLineSection(lineSection);
    return line;
  }

  public List<Line> findAllLines() {
    return lineReader.read();
  }

  public Line findLineById(Long id) {
    return lineReader.readById(id);
  }

  public Line updateLineById(Long id, UpdateLineRequest request) {
    return lineModifier.modify(id, request);
  }

  public void deleteLineById(Long id) {
    lineRemover.remove(id);
  }
}

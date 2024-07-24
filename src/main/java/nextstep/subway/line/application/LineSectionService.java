package nextstep.subway.line.application;

import lombok.RequiredArgsConstructor;
import nextstep.subway.line.application.dto.AppendLineSectionRequest;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineSection;
import nextstep.subway.station.application.StationReader;
import nextstep.subway.station.domain.Station;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LineSectionService {
  private final LineReader lineReader;
  private final LineSectionMapper lineSectionMapper;
  private final StationReader stationReader;

  @Transactional
  public Line appendLineSection(Long lineId, AppendLineSectionRequest request) {
    Line line = lineReader.readById(lineId);
    LineSection lineSection = lineSectionMapper.map(request);
    line.addLineSection(lineSection);
    return line;
  }

  @Transactional
  public void removeLineSection(Long lineId, Long stationId) {
    Line line = lineReader.readById(lineId);
    Station station = stationReader.readById(stationId);
    line.remove(station);
  }
}

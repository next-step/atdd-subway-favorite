package nextstep.subway.line.application;

import lombok.RequiredArgsConstructor;
import nextstep.subway.line.application.dto.AppendLineSectionRequest;
import nextstep.subway.line.domain.LineSection;
import nextstep.subway.station.application.StationReader;
import nextstep.subway.station.domain.Station;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LineSectionMapper {
  private final StationReader stationReader;

  public LineSection map(AppendLineSectionRequest request) {
    Station upStation = stationReader.readById(request.getUpStationId());
    Station downStation = stationReader.readById(request.getDownStationId());
    return new LineSection(upStation, downStation, request.getDistance());
  }
}

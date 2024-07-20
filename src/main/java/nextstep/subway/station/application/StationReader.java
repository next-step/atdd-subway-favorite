package nextstep.subway.station.application;

import lombok.RequiredArgsConstructor;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import nextstep.subway.station.exception.StationNotFoundException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StationReader {
  private final StationRepository stationRepository;

  public Station readById(Long id) {
    return stationRepository.findById(id).orElseThrow(() -> new StationNotFoundException(id));
  }
}

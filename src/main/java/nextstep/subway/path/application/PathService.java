package nextstep.subway.path.application;

import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.domain.Lines;
import nextstep.subway.path.application.dto.PathRequest;
import nextstep.subway.path.application.dto.PathResponse;
import nextstep.subway.path.domain.Path;
import nextstep.subway.path.domain.PathFinder;
import nextstep.subway.station.application.dto.StationResponseFactory;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class PathService {
    private final PathFinder pathFinder;
    private final LineRepository lineRepository;
    private final StationRepository stationRepository;

    public PathService(PathFinder pathFinder,
                       LineRepository lineRepository,
                       StationRepository stationRepository) {
        this.pathFinder = pathFinder;
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }

    public PathResponse findShortCut(PathRequest pathRequest) {
        Path path = pathFinder.shortcut(Lines.from(lineRepository.findAllFetchJoin()),
                getStation(pathRequest.getSource()),
                getStation(pathRequest.getTarget()));
        return new PathResponse(StationResponseFactory.create(path.getStations()), path.getDistance());
    }

    private Station getStation(Long stationId) {
        return stationRepository.findById(stationId).orElseThrow(() -> new IllegalArgumentException("해당 지하철역 정보를 찾지 못했습니다."));
    }
}

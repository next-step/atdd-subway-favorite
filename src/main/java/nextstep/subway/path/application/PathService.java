package nextstep.subway.path.application;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.path.domain.ShortestPath;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.path.exception.SameSourceAndTargetStationException;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import nextstep.subway.station.dto.StationResponse;
import nextstep.subway.station.exception.StationNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PathService {
    private final StationRepository stationRepository;
    private final LineRepository lineRepository;

    public PathService(StationRepository stationRepository, LineRepository lineRepository) {
        this.stationRepository = stationRepository;
        this.lineRepository = lineRepository;
    }

    public PathResponse searchPath(Long source, Long target) {
        ShortestPath shortestPath = createShortestPath(source, target);

        List<StationResponse> stationResponses = shortestPath.getPath().stream()
                .map(StationResponse::from)
                .collect(Collectors.toList());

        return new PathResponse(stationResponses, shortestPath.getDistance());
    }

    private ShortestPath createShortestPath(Long source, Long target) {
        validateSourceAndTargetId(source, target);
        validateExistenceOfStation(source, target);

        Station sourceStation = findStation(source);
        Station targetStation = findStation(target);

        List<Line> lines = lineRepository.findAll();

        return new ShortestPath(lines, sourceStation, targetStation);
    }

    private void validateSourceAndTargetId(Long source, Long target) {
        if (source.equals(target)) {
            throw new SameSourceAndTargetStationException();
        }
    }

    private void validateExistenceOfStation(Long source, Long target) {
        if (!stationRepository.existsById(source) || !stationRepository.existsById(target)) {
            throw new StationNotFoundException();
        }
    }

    private Station findStation(Long source) {
        return stationRepository.findById(source)
                .orElseThrow(StationNotFoundException::new);
    }

    public void validatePathConnection(Long source, Long target) {
        createShortestPath(source, target);
    }
}

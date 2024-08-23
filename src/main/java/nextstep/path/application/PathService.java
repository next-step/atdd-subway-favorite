package nextstep.path.application;

import nextstep.line.domain.SectionRepository;
import nextstep.path.application.dto.PathsResponse;
import nextstep.path.domain.ShortestPath;
import nextstep.path.ui.exception.SameSourceAndTargetException;
import nextstep.station.application.StationService;
import nextstep.station.application.dto.StationResponse;
import nextstep.station.domain.Station;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class PathService {

    private SectionRepository sectionRepository;
    private StationService stationService;

    public PathService(SectionRepository sectionRepository, StationService stationService) {
        this.sectionRepository = sectionRepository;
        this.stationService = stationService;
    }

    public PathsResponse findShortestPaths(Long source, Long target) {
        ShortestPath shortestPath = createShortestPath(source, target);

        Station start = stationService.lookUp(source);
        Station end = stationService.lookUp(target);

        return new PathsResponse(shortestPath.getDistance(start, end), createStationResponses(shortestPath.getStations(start, end)));
    }

    public void validatePaths(Long source, Long target) {
        ShortestPath shortestPath = createShortestPath(source, target);

        Station start = stationService.lookUp(source);
        Station end = stationService.lookUp(target);

        shortestPath.validateContains(start, end);
        shortestPath.validateConnected(start, end);
    }

    private ShortestPath createShortestPath(Long source, Long target) {
        validateSameSourceAndTarget(source, target);
        return ShortestPath.from(sectionRepository.findAll());
    }

    private static void validateSameSourceAndTarget(Long source, Long target) {
        if (source.equals(target)) {
            throw new SameSourceAndTargetException();
        }
    }

    private List<StationResponse> createStationResponses(List<Station> stations) {
        return stations.stream()
                .map(StationResponse::from)
                .collect(Collectors.toList());
    }
}

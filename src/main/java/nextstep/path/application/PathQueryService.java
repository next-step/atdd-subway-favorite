package nextstep.path.application;

import nextstep.exceptions.ErrorMessage;
import nextstep.line.domain.Line;
import nextstep.line.repository.LineRepository;
import nextstep.path.domain.LineSectionEdge;
import nextstep.path.exceptions.PathNotFoundException;
import nextstep.path.payload.SearchPathRequest;
import nextstep.path.payload.ShortestPathResponse;
import nextstep.station.domain.Station;
import nextstep.station.exception.NonExistentStationException;
import nextstep.station.payload.StationResponse;
import nextstep.station.repository.StationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@Service
public class PathQueryService {

    private final StationRepository stationRepository;
    private final LineRepository lineRepository;
    private final ShortestPathFinder<LineSectionEdge, Long> shortestPathFinder;

    public PathQueryService(final StationRepository stationRepository, final LineRepository lineRepository, final ShortestPathFinder<LineSectionEdge, Long> shortestPathFinder) {
        this.stationRepository = stationRepository;
        this.lineRepository = lineRepository;
        this.shortestPathFinder = shortestPathFinder;
    }

    public ShortestPathResponse findShortestPath(final SearchPathRequest request) {
        Long source = request.getSource();
        Long target = request.getTarget();
        assertStationExist(source, target);
        List<LineSectionEdge> edges = lineRepository.findAll().stream()
                .flatMap(Line::sectionStream)
                .map(LineSectionEdge::from)
                .collect(Collectors.toList());

        var shortestPath = shortestPathFinder.find(edges, source, target)
                .orElseThrow(() -> new PathNotFoundException(ErrorMessage.PATH_NOT_FOUND));
        List<Long> stationIds = shortestPath.getVertexList();
        Map<Long, Station> stationMap = getStationMap(stationIds);
        return new ShortestPathResponse(
                stationIds.stream()
                        .map(stationMap::get)
                        .map(StationResponse::from)
                        .collect(Collectors.toList()),
                (long) shortestPath.getWeight()
        );
    }

    private void assertStationExist(final Long source, final Long target) {
        List<Station> stations = stationRepository.findByIdIn(List.of(source, target));
        if (stations.size() != 2) {
            throw new NonExistentStationException(ErrorMessage.NON_EXISTENT_STATION);
        }
    }

    private Map<Long, Station> getStationMap(final Collection<Long> stationsIds) {
        return stationRepository.findByIdIn(stationsIds)
                .stream()
                .collect(Collectors.toMap(Station::getId, (station -> station)));
    }
}

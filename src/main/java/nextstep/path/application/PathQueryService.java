package nextstep.path.application;

import nextstep.exceptions.ErrorMessage;
import nextstep.line.domain.Line;
import nextstep.line.repository.LineRepository;
import nextstep.path.domain.LineSectionEdge;
import nextstep.path.exceptions.PathNotFoundException;
import nextstep.path.payload.ShortestPathResponse;
import nextstep.station.domain.Station;
import nextstep.station.exception.NonExistentStationException;
import nextstep.station.payload.StationMapper;
import nextstep.station.payload.StationResponse;
import nextstep.station.repository.StationRepository;
import org.jgrapht.GraphPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@Service
public class PathQueryService {

    private final StationRepository stationRepository;
    private final LineRepository lineRepository;
    private final ShortestPathFinder<LineSectionEdge, Long> shortestPathFinder;
    private final StationMapper stationMapper;

    public PathQueryService(final StationRepository stationRepository,
                            final LineRepository lineRepository,
                            final ShortestPathFinder<LineSectionEdge, Long> shortestPathFinder,
                            final StationMapper stationMapper) {
        this.stationRepository = stationRepository;
        this.lineRepository = lineRepository;
        this.shortestPathFinder = shortestPathFinder;
        this.stationMapper = stationMapper;
    }

    public ShortestPathResponse getShortestPath(final Long source, final Long target) {
        GraphPath<Long, DefaultWeightedEdge> shortestPath = findShortestPath(source, target)
                .orElseThrow(() -> new PathNotFoundException(ErrorMessage.PATH_NOT_FOUND));

        List<Long> stationIds = shortestPath.getVertexList();
        Map<Long, StationResponse> stationMap = stationMapper.getStationResponseMap(stationIds);
        return new ShortestPathResponse(
                stationIds.stream()
                        .map(stationMap::get)
                        .collect(Collectors.toList()),
                (long) shortestPath.getWeight()
        );
    }

    public Optional<GraphPath<Long, DefaultWeightedEdge>> findShortestPath(final Long source, final Long target) {
        assertStationExist(source, target);
        List<LineSectionEdge> edges = lineRepository.findAll().stream()
                .flatMap(Line::sectionStream)
                .map(LineSectionEdge::from)
                .collect(Collectors.toList());

        return shortestPathFinder.find(edges, source, target);
    }

    private void assertStationExist(final Long source, final Long target) {
        List<Station> stations = stationRepository.findByIdIn(List.of(source, target));
        if (stations.size() != 2) {
            throw new NonExistentStationException(ErrorMessage.NON_EXISTENT_STATION);
        }
    }

}

package nextstep.path.repository;

import nextstep.line.domain.Line;
import nextstep.line.repository.LineRepository;
import nextstep.path.application.ShortestPathFinder;
import nextstep.path.domain.LineSectionEdge;
import nextstep.path.domain.SearchPath;
import nextstep.path.payload.ShortestPathResponse;
import nextstep.station.payload.StationMapper;
import nextstep.station.payload.StationResponse;
import org.jgrapht.GraphPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class PathResolver {
    private final Map<SearchPath, ShortestPathResponse> pathMap = new HashMap<>();
    private final LineRepository lineRepository;
    private final ShortestPathFinder<LineSectionEdge, Long> shortestPathFinder;
    private final StationMapper stationMapper;


    public PathResolver(final LineRepository lineRepository, final ShortestPathFinder<LineSectionEdge, Long> shortestPathFinder, final StationMapper stationMapper) {
        this.lineRepository = lineRepository;
        this.shortestPathFinder = shortestPathFinder;
        this.stationMapper = stationMapper;
    }

    public Optional<ShortestPathResponse> get(final Long source, final Long target) {
        SearchPath searchPath = new SearchPath(source, target);
        ShortestPathResponse path = pathMap.get(searchPath);
        if (path == null) {
            List<LineSectionEdge> edges = getLineSectionEdges();
            shortestPathFinder.find(edges, source, target)
                    .map(this::generateShortestPathResponse)
                    .ifPresent(it -> pathMap.put(searchPath, it));
        }
        return Optional.ofNullable(pathMap.get(searchPath));
    }

    public void removeAll() {
        pathMap.clear();
    }

    private List<LineSectionEdge> getLineSectionEdges() {
        return lineRepository.findAll().stream()
                .flatMap(Line::sectionStream)
                .map(LineSectionEdge::from)
                .collect(Collectors.toList());
    }

    private ShortestPathResponse generateShortestPathResponse(final GraphPath<Long, DefaultWeightedEdge> path) {
        List<Long> stationIds = path.getVertexList();
        Map<Long, StationResponse> stationMap = stationMapper.getStationResponseMap(stationIds);
        return new ShortestPathResponse(
                stationIds.stream()
                        .map(stationMap::get)
                        .collect(Collectors.toList()),
                (long) path.getWeight());
    }


}

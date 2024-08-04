package nextstep.path.repository;

import nextstep.line.domain.Line;
import nextstep.line.repository.LineRepository;
import nextstep.path.application.ShortestPathFinder;
import nextstep.path.domain.LineSectionEdge;
import nextstep.path.domain.SearchPath;
import nextstep.path.payload.ShortestPathResponse;
import nextstep.station.payload.StationMapper;
import nextstep.station.payload.StationResponse;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class PathRepository {
    private final Map<SearchPath, ShortestPathResponse> pathMap = new HashMap<>();
    private final LineRepository lineRepository;
    private final ShortestPathFinder<LineSectionEdge, Long> shortestPathFinder;
    private final StationMapper stationMapper;


    public PathRepository(final LineRepository lineRepository, final ShortestPathFinder<LineSectionEdge, Long> shortestPathFinder, final StationMapper stationMapper) {
        this.lineRepository = lineRepository;
        this.shortestPathFinder = shortestPathFinder;
        this.stationMapper = stationMapper;
    }

    public Optional<ShortestPathResponse> get(final Long source, final Long target) {
        SearchPath searchPath = new SearchPath(source, target);
        ShortestPathResponse path = pathMap.get(searchPath);
        if (path == null) {
            List<LineSectionEdge> edges = lineRepository.findAll().stream()
                    .flatMap(Line::sectionStream)
                    .map(LineSectionEdge::from)
                    .collect(Collectors.toList());

            shortestPathFinder.find(edges, searchPath.getSource(), searchPath.getTarget())
                    .map(it -> {
                        List<Long> stationIds = it.getVertexList();
                        Map<Long, StationResponse> stationMap = stationMapper.getStationResponseMap(stationIds);
                        return new ShortestPathResponse(
                                stationIds.stream()
                                        .map(stationMap::get)
                                        .collect(Collectors.toList()),
                                (long) it.getWeight());

                    })
                    .ifPresent(it -> pathMap.put(searchPath, it));
        }
        return Optional.ofNullable(pathMap.get(searchPath));
    }

    public void removeAll() {
        pathMap.clear();
    }

}

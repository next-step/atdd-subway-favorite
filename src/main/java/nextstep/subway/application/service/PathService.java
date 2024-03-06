package nextstep.subway.application.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import nextstep.subway.application.dto.response.PathResponse;
import nextstep.subway.application.exception.CanNotFindPathException;
import nextstep.subway.domain.entity.Line;
import nextstep.subway.domain.entity.Path;
import nextstep.subway.domain.entity.PathFinder;
import nextstep.subway.domain.entity.Station;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class PathService {

    private final StationService stationService;
    private final LineService lineService;

    public PathResponse findPath(Long sourceStationId, Long targetStationId) {
        Station source = stationService.getStationById(sourceStationId);
        Station target = stationService.getStationById(targetStationId);
        List<Line> lines = lineService.getLines();
        PathFinder pathFinder = new PathFinder(lines);
        Path shortestPath = pathFinder.findShortestPath(source, target)
            .orElseThrow(() -> new CanNotFindPathException("Unable to find the shortest path."));
        return new PathResponse(shortestPath);
    }


}

package nextstep.subway.path.application;

import nextstep.subway.line.application.LineService;
import nextstep.subway.line.domain.Line;
import nextstep.subway.path.domain.PathResult;
import nextstep.subway.path.dto.PathResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PathService {
    private LineService lineService;
    private PathFinder pathFinder;

    public PathService(LineService lineService, PathFinder pathFinder) {
        this.lineService = lineService;
        this.pathFinder = pathFinder;
    }

    public PathResponse findPath(Long source, Long target) {
        List<Line> lines = lineService.findLines();
        PathResult pathResult = pathFinder.findPath(lines, source, target);
        return PathResponse.of(pathResult, lines);
    }
}

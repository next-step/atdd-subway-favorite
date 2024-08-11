package nextstep.subway.application;

import lombok.RequiredArgsConstructor;
import nextstep.common.exception.PathNotFoundException;
import nextstep.subway.domain.PathFinderService;
import nextstep.subway.domain.PathResult;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class PathService {
    private final PathFinderService pathFinderService;

    public PathResponse getPath(Long sourceId, Long targetId) {
        if (!pathFinderService.isValidPath(sourceId, targetId)) {
            throw new PathNotFoundException(sourceId, targetId);
        }

        PathResult pathResult = pathFinderService.findPath(sourceId, targetId);
        return PathResponse.of(pathResult.getPathStations(), pathResult.getTotalDistance());
    }
}

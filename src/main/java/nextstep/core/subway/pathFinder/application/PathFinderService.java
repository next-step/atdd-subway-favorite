package nextstep.core.subway.pathFinder.application;

import nextstep.core.subway.line.application.LineService;
import nextstep.core.subway.pathFinder.application.converter.PathFinderConverter;
import nextstep.core.subway.pathFinder.application.dto.PathFinderRequest;
import nextstep.core.subway.pathFinder.application.dto.PathFinderResponse;
import org.springframework.stereotype.Service;

@Service
public class PathFinderService {

    private final LineService lineService;

    private final PathFinder pathFinder;

    public PathFinderService(LineService lineService, PathFinder pathFinder) {
        this.lineService = lineService;
        this.pathFinder = pathFinder;
    }

    public PathFinderResponse findShortestPath(PathFinderRequest pathFinderRequest) {
        validatePathRequest(pathFinderRequest);

        return PathFinderConverter.convertToResponse(pathFinder.calculateShortestPath(
                lineService.findAllLines(),
                lineService.findStation(pathFinderRequest.getDepartureStationId()),
                lineService.findStation(pathFinderRequest.getArrivalStationId())));
    }

    public boolean isValidPath(PathFinderRequest pathFinderRequest) {
        validatePathRequest(pathFinderRequest);

        return pathFinder.existPathBetweenStations(
                lineService.findAllLines(),
                lineService.findStation(pathFinderRequest.getDepartureStationId()),
                lineService.findStation(pathFinderRequest.getArrivalStationId()));
    }

    private void validatePathRequest(PathFinderRequest pathFinderRequest) {
        if (areStationsSame(pathFinderRequest)) {
            throw new IllegalArgumentException("출발역과 도착역이 동일할 수 없습니다.");
        }
    }

    private boolean areStationsSame(PathFinderRequest pathFinderRequest) {
        return pathFinderRequest.getDepartureStationId().equals(pathFinderRequest.getArrivalStationId());
    }
}

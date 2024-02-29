package nextstep.core.subway.pathFinder.presentation;

import nextstep.core.subway.pathFinder.application.PathFinderService;
import nextstep.core.subway.pathFinder.application.dto.PathFinderRequest;
import nextstep.core.subway.pathFinder.application.dto.PathFinderResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PathFinderController {

    public final PathFinderService pathFinderService;

    public PathFinderController(PathFinderService pathFinderService) {
        this.pathFinderService = pathFinderService;
    }

    @GetMapping("/paths")
    public ResponseEntity<PathFinderResponse> findShortestPath(@RequestParam("source") Long departureStationId,
                                                               @RequestParam("target") Long arrivalStationId) {
        PathFinderRequest pathFinderRequest = new PathFinderRequest(departureStationId, arrivalStationId);
        return ResponseEntity.ok(pathFinderService.findShortestPath(pathFinderRequest));
    }
}

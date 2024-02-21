package nextstep.path.ui;

import nextstep.path.application.PathService;
import nextstep.path.application.dto.PathDto;
import nextstep.path.ui.dto.PathResponseBody;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/paths")
@RestController
public class PathController {
    private final PathService pathService;

    public PathController(PathService pathService) {
        this.pathService = pathService;
    }

    @GetMapping
    public ResponseEntity<PathResponseBody> findPaths(
            @RequestParam("source") Long sourceStationId,
            @RequestParam("target") Long targetStationId) {
        PathDto path = pathService.findShortestPath(sourceStationId, targetStationId);
        return ResponseEntity.ok().body(PathResponseBody.create(path));
    }
}

package nextstep.path.ui;

import nextstep.path.application.dto.PathResponse;
import nextstep.path.application.PathService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PathController {
    private final PathService pathService;

    public PathController(PathService pathService) {
        this.pathService = pathService;
    }

    @GetMapping(value = "/paths")
    public ResponseEntity<PathResponse> getPath(@RequestParam("source") Long source,  @RequestParam("target") Long target) {
        PathResponse path = pathService.getShortestPath(source, target);
        return ResponseEntity.ok().body(path);
    }

}

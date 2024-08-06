package nextstep.path.ui;

import nextstep.path.application.PathService;
import nextstep.path.application.dto.PathsResponse;
import nextstep.path.ui.exception.SameSourceAndTargetException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PathController {

    private PathService pathService;

    public PathController(PathService pathService) {
        this.pathService = pathService;
    }

    @GetMapping("/paths")
    public ResponseEntity<PathsResponse> findShortestPath(@RequestParam("source") Long source, @RequestParam("target") Long target) {
        if (source.equals(target)) {
            throw new SameSourceAndTargetException();
        }
        return ResponseEntity.ok()
                .body(pathService.findShortestPaths(source, target));
    }
}

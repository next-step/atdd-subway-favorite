package atdd.path.web;

import atdd.path.application.GraphService;
import atdd.path.application.dto.path.PathResponseView;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PathController {
    private GraphService graphService;

    public PathController(GraphService graphService) {
        this.graphService = graphService;
    }

    @GetMapping("/paths")
    public ResponseEntity findPath(@RequestParam Long startId, @RequestParam Long endId) {
        return ResponseEntity.ok(new PathResponseView(startId, endId, graphService.findPath(startId, endId)));
    }
}

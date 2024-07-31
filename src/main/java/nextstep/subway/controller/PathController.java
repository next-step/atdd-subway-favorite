package nextstep.subway.controller;

import nextstep.subway.dto.PathRequest;
import nextstep.subway.dto.PathResponse;
import nextstep.subway.service.PathService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/paths")
public class PathController {
    private final PathService pathService;

    public PathController(PathService pathService) {
        this.pathService = pathService;
    }

    @GetMapping
    public ResponseEntity<PathResponse> getPath(PathRequest pathRequest) {
        return ResponseEntity.ok().body(pathService.getPath(pathRequest));
    }
}

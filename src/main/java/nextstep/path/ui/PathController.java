package nextstep.path.ui;

import nextstep.path.application.PathService;
import nextstep.path.application.dto.PathResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PathController {
    private final PathService pathService;

    public PathController(PathService pathService) {
        this.pathService = pathService;
    }

    @GetMapping("/paths")
    public PathResponse findPath(@RequestParam Long source, @RequestParam Long target) {
        return pathService.findPath(source, target);
    }
}

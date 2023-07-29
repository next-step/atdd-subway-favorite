package nextstep.subway.ui;

import lombok.RequiredArgsConstructor;
import nextstep.subway.dto.ShortestPathResponse;
import nextstep.subway.service.PathService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PathController {

    private final PathService pathService;

    @GetMapping("/paths")
    ResponseEntity getShortestPath(@RequestParam("source") long sourceStationId, @RequestParam("target") long targetStationId) {
        ShortestPathResponse shortestPathResponse = pathService.getShortestPath(sourceStationId, targetStationId);
        return ResponseEntity.ok(shortestPathResponse);
    }
}

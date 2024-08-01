package nextstep.subway.controller;

import lombok.RequiredArgsConstructor;
import nextstep.subway.domain.query.PathReader;
import nextstep.subway.domain.view.PathView;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/paths")
@RequiredArgsConstructor
public class PathController {

    private final PathReader pathReader;

    @GetMapping("")
    public ResponseEntity<PathView.Main> getPath(
            @RequestParam Long source,
            @RequestParam Long target
    ) {
        return ResponseEntity.ok().body(pathReader.findShortestPath(source, target));
    }
}

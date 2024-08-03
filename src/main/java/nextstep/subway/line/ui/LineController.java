package nextstep.subway.line.ui;

import nextstep.subway.line.application.dto.LineRequest;
import nextstep.subway.line.application.dto.LineResponse;
import nextstep.subway.line.application.dto.SectionRequest;
import nextstep.subway.line.application.LineService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/lines")
public class LineController {
    private final LineService lineService;

    public LineController(LineService lineService) {
        this.lineService = lineService;
    }

    @PostMapping
    public ResponseEntity<LineResponse> createLine(@RequestBody LineRequest lineRequest) {
        LineResponse lineResponse = lineService.saveLine(lineRequest);
        return ResponseEntity.created(URI.create("/lines/" + lineResponse.getId())).body(lineResponse);
    }

    @GetMapping
    public ResponseEntity<List<LineResponse>> showLines() {
        return ResponseEntity.ok().body(lineService.findAllLines());
    }

    @GetMapping("/{id}")
    public ResponseEntity<LineResponse> showLine(@PathVariable Long id) {
        return ResponseEntity.ok().body(lineService.findLine(id));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Void> modifyLine(@PathVariable Long id, @RequestBody LineRequest lineRequest) {
        lineService.updateLine(id, lineRequest);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLine(@PathVariable Long id) {
        lineService.removeLine(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/sections")
    public ResponseEntity<LineResponse> createLineSection(@PathVariable Long id, @RequestBody SectionRequest sectionRequest) {
        LineResponse lineResponse = lineService.addSection(id, sectionRequest);
        return ResponseEntity.created(URI.create("/lines/" + lineResponse.getId() + "/sections")).body(lineResponse);
    }

    @DeleteMapping("/{id}/sections")
    public ResponseEntity<LineResponse> deleteLineSection(@PathVariable Long id, @RequestParam Long stationId) {
        lineService.removeSection(id, stationId);
        return ResponseEntity.noContent().build();
    }
}

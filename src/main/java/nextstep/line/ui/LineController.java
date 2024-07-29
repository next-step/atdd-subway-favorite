package nextstep.line.ui;

import nextstep.line.payload.AddSectionRequest;
import nextstep.line.payload.CreateLineRequest;
import nextstep.line.payload.LineResponse;
import nextstep.line.payload.UpdateLineRequest;
import nextstep.line.service.LineCommandService;
import nextstep.line.service.LineQueryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RequestMapping("/lines")
@RestController
public class LineController {

    private final LineCommandService lineCommandService;
    private final LineQueryService lineQueryService;

    public LineController(final LineCommandService lineCommandService, final LineQueryService lineQueryService) {
        this.lineCommandService = lineCommandService;
        this.lineQueryService = lineQueryService;
    }

    @PostMapping
    public ResponseEntity<LineResponse> createLine(@RequestBody CreateLineRequest request) {
        var response = lineCommandService.saveLine(request);
        return ResponseEntity.created(URI.create("/lines/" + response.getId())).body(response);
    }

    @GetMapping
    public ResponseEntity<List<LineResponse>> showLines() {
        return ResponseEntity.ok(lineQueryService.getLines());
    }

    @GetMapping("/{id}")
    public ResponseEntity<LineResponse> showLine(@PathVariable Long id) {
        return ResponseEntity.ok(lineQueryService.getLine(id));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Void> updateLine(@PathVariable Long id, @RequestBody UpdateLineRequest request) {
        lineCommandService.modify(id, request);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLine(@PathVariable Long id) {
        lineCommandService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/sections")
    public ResponseEntity<Void> addSections(@PathVariable Long id, @RequestBody AddSectionRequest request) {
        lineCommandService.addSection(id, request);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}/sections")
    public ResponseEntity<Void> addSections(@PathVariable Long id, Long stationId) {
        lineCommandService.removeSection(id, stationId);
        return ResponseEntity.noContent().build();
    }

}

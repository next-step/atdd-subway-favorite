package nextstep.subway.domain.line;

import nextstep.subway.domain.section.dto.SectionCreateRequest;
import nextstep.subway.domain.section.dto.SectionResponse;
import nextstep.subway.domain.line.dto.LineCreateRequest;
import nextstep.subway.domain.line.dto.LineResponse;
import nextstep.subway.domain.line.dto.LineUpdateRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
public class LineController {
    private LineService lineService;

    public LineController(LineService lineService) {
        this.lineService = lineService;
    }

    @PostMapping("/lines")
    public ResponseEntity<LineResponse> createLine(@RequestBody LineCreateRequest request) {
        LineResponse line = lineService.saveLine(request);
        return ResponseEntity.created(URI.create("/lines/" + line.getId())).body(line);
    }

    @GetMapping("/lines")
    public ResponseEntity<List<LineResponse>> findAllLines() {
        List<LineResponse> lines = lineService.findLines();
        return ResponseEntity.ok(lines);
    }

    @GetMapping("/lines/{id}")
    public ResponseEntity<LineResponse> findLineById(@PathVariable Long id) {
        LineResponse line = lineService.findLineById(id);
        return ResponseEntity.ok(line);
    }

    @PutMapping("/lines/{id}")
    public ResponseEntity<LineResponse> updateLine(@PathVariable Long id, @RequestBody LineUpdateRequest lineRequest) {
        lineService.updateLine(id, lineRequest);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/lines/{id}")
    public ResponseEntity<Void> deleteLine(@PathVariable Long id) {
        lineService.deleteLine(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/lines/{id}/sections")
    public ResponseEntity<SectionResponse> addSection(@PathVariable Long id, @RequestBody SectionCreateRequest request) {
        SectionResponse response = lineService.addSection(id, request);
        return ResponseEntity.created(URI.create("")).body(response);
    }

    @DeleteMapping("/lines/{lineId}/sections")
    public ResponseEntity<Void> deleteSection(@PathVariable Long lineId, @RequestParam Long stationId) {
        lineService.deleteSection(lineId, stationId);
        return ResponseEntity.noContent().build();
    }
}

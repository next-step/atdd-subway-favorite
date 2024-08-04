package nextstep.line.ui;

import nextstep.line.application.dto.LineRequest;
import nextstep.line.application.dto.LineResponse;
import nextstep.line.application.LineService;
import nextstep.line.application.dto.SectionRequest;
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
    public ResponseEntity<LineResponse> createLine(@RequestBody LineRequest lineRequest) {
        LineResponse line = lineService.lineSave(lineRequest);
        return ResponseEntity.created(URI.create("/lines/" + line.getId()))
                .body(line);
    }

    @GetMapping("/lines")
    public ResponseEntity<List<LineResponse>> showLines() {
        return ResponseEntity.ok()
                .body(lineService.findAllLines());
    }

    @GetMapping("/lines/{id}")
    public ResponseEntity<LineResponse> showLines(@PathVariable("id") Long id) {
        return ResponseEntity.ok()
                .body(lineService.lookUpLine(id));
    }

    @PutMapping("/lines/{id}")
    public ResponseEntity<LineResponse> modifyLines(@PathVariable("id") Long id, @RequestBody LineRequest lineRequest) {
        return ResponseEntity.ok()
                .body(lineService.modifyLine(id, lineRequest));
    }

    @DeleteMapping("/lines/{id}")
    public ResponseEntity<LineResponse> deleteLines(@PathVariable("id") Long id) {
        lineService.deleteLine(id);
        return ResponseEntity.noContent()
                .build();
    }

    @PostMapping("/lines/{id}/sections")
    public ResponseEntity<LineResponse> addSections(@PathVariable("id") Long id, @RequestBody SectionRequest sectionRequest) {
        return ResponseEntity.created(URI.create("/lines/" + id))
                .body(lineService.addSections(id, sectionRequest));
    }

    @DeleteMapping("/lines/{id}/sections")
    public ResponseEntity<LineResponse> deleteSections(@PathVariable("id") Long id, @RequestParam("stationId") Long stationId) {
        return ResponseEntity.ok()
                .body(lineService.deleteSection(id, stationId));
    }
}

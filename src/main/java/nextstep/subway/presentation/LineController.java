package nextstep.subway.presentation;

import lombok.RequiredArgsConstructor;
import nextstep.subway.application.LineService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/lines")
@RestController
public class LineController {
    private final LineService lineService;

    @PostMapping
    public ResponseEntity<LineResponse> createLine(@RequestBody LineRequest lineRequest) {
        LineResponse line = lineService.saveLine(lineRequest);
        return ResponseEntity.created(URI.create("/lines/" + line.getId())).body(line);
    }

    @GetMapping
    public ResponseEntity<List<LineResponse>> retrieveAllLines() {
        return ResponseEntity.ok().body(lineService.findAllLines());
    }

    @GetMapping("/{lineId}")
    public ResponseEntity<LineResponse> retrieveLines(@PathVariable Long lineId) {
        return ResponseEntity.ok().body(lineService.findLineById(lineId));
    }

    @PutMapping("/{lineId}")
    public ResponseEntity<Void> updateLine(@PathVariable Long lineId, @RequestBody LineUpdateRequest lineUpdateRequest) {
        lineService.updateLine(lineId, lineUpdateRequest);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{lineId}")
    public ResponseEntity<Void> deleteLine(@PathVariable Long lineId) {
        lineService.deleteLine(lineId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{lineId}/stations/{stationId}")
    public ResponseEntity<Void> deleteStationFromLine(@PathVariable Long lineId, @PathVariable Long stationId) {
        lineService.deleteStationFromLine(lineId, stationId);
        return ResponseEntity.noContent().build();
    }
}

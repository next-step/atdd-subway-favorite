package nextstep.subway.line.ui;

import nextstep.subway.line.application.LineStationService;
import nextstep.subway.line.dto.LineStationCreateRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/lines")
public class LineStationController {
    private final LineStationService lineStationService;

    public LineStationController(LineStationService lineStationService) {
        this.lineStationService = lineStationService;
    }

    @PostMapping("/{lineId}/stations")
    public ResponseEntity createLineStation(@PathVariable Long lineId, @RequestBody LineStationCreateRequest edgeCreateRequest) {
        lineStationService.addLineStation(lineId, edgeCreateRequest);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping("/{lineId}/stations/{stationId}")
    public ResponseEntity deleteLineStation(@PathVariable Long lineId, @PathVariable Long stationId) {
        lineStationService.removeLineStation(lineId, stationId);
        return ResponseEntity.noContent().build();
    }
}

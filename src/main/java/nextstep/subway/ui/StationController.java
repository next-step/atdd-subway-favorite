package nextstep.subway.ui;

import nextstep.subway.application.StationService;
import nextstep.subway.application.dto.station.StationRequest;
import nextstep.subway.application.dto.station.StationResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StationController {

    private final StationService stationService;

    public StationController(StationService stationService) { this.stationService = stationService; }

    @PostMapping("/stations")
    public ResponseEntity<StationResponse> createStation(@RequestBody StationRequest stationRequest) {
        StationResponse response = stationService.createStation(stationRequest);
        return ResponseEntity.ok().body(response);
    }
}
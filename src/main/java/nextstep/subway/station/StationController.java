package nextstep.subway.station;

import nextstep.subway.common.SuccessResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/stations")
@RestController
public class StationController {
    private StationService stationService;

    public StationController(StationService stationService) {
        this.stationService = stationService;
    }

    @PostMapping
    public ResponseEntity<StationResponse> createStation(@RequestBody StationRequest stationRequest) {
        StationResponse station = stationService.saveStation(stationRequest);

        return SuccessResponse.created(station, () -> "/lines/" + station.getId());
    }

    @GetMapping
    public ResponseEntity<List<StationResponse>> showStations() {
        return SuccessResponse.ok(stationService.findAllStations());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStation(@PathVariable Long id) {
        stationService.deleteStationById(id);
        return SuccessResponse.noContent();
    }
}

package nextstep.subway.ui;

import nextstep.subway.application.StationService;
import nextstep.subway.application.request.CreateStationRequest;
import nextstep.subway.application.response.CreateStationResponse;
import nextstep.subway.application.response.ShowAllStationsResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
public class StationController {
    private StationService stationService;

    public StationController(StationService stationService) {
        this.stationService = stationService;
    }

    @PostMapping("/stations")
    public ResponseEntity<CreateStationResponse> createStation(@RequestBody CreateStationRequest createStationRequest) {
        CreateStationResponse station = stationService.saveStation(createStationRequest);
        return ResponseEntity.created(URI.create("/stations/" + station.getStationId())).body(station);
    }

    @GetMapping(value = "/stations")
    public ResponseEntity<ShowAllStationsResponse> showStations() {
        return ResponseEntity.ok().body(stationService.findAllStations());
    }

    @DeleteMapping("/stations/{stationId}")
    public ResponseEntity<Void> deleteStation(@PathVariable Long stationId) {
        stationService.deleteStationById(stationId);
        return ResponseEntity.noContent().build();
    }

}

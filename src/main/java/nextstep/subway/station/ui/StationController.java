package nextstep.subway.station.ui;

import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.application.dto.StationRequest;
import nextstep.subway.station.application.dto.StationResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class StationController {
  private final StationService stationService;

  @PostMapping("/stations")
  public ResponseEntity<StationResponse> createStation(@RequestBody StationRequest stationRequest) {
    StationResponse station = stationService.saveStation(stationRequest);
    return ResponseEntity.created(URI.create("/stations/" + station.getId())).body(station);
  }

  @GetMapping(value = "/stations")
  public ResponseEntity<List<StationResponse>> showStations() {
    return ResponseEntity.ok().body(stationService.findAllStations());
  }

  @DeleteMapping("/stations/{id}")
  public ResponseEntity<Void> deleteStation(@PathVariable Long id) {
    stationService.deleteStationById(id);
    return ResponseEntity.noContent().build();
  }
}

package nextstep.subway.station.ui;

import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import nextstep.subway.station.dto.StationCreateRequest;
import nextstep.subway.station.dto.StationResponse;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
public class StationController {
    private final StationRepository stationRepository;

    public StationController(StationRepository stationRepository) {
        this.stationRepository = stationRepository;
    }

    @PostMapping("/stations")
    public ResponseEntity<StationResponse> createStation(@RequestBody StationCreateRequest view) {
        Station station = view.toStation();
        try {
            Station persistStation = stationRepository.save(station);
            return ResponseEntity.created(URI.create("/stations/" + persistStation.getId())).body(StationResponse.of(persistStation));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping(value = "/stations", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Station>> showStations() {
        return ResponseEntity.ok().body(stationRepository.findAll());
    }

    @DeleteMapping("/stations/{id}")
    public ResponseEntity<Void> deleteStation(@PathVariable Long id) {
        stationRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}

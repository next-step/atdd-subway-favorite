package atdd.path.web;

import atdd.path.domain.Station;
import atdd.path.application.dto.CreateStationRequestView;
import atdd.path.application.dto.StationResponseView;
import atdd.path.dao.StationDao;
import atdd.path.repository.StationRepository;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
public class StationController {
    private StationRepository stationRepository;

    public StationController(StationRepository stationRepository) {
        this.stationRepository = stationRepository;
    }

    @PostMapping("/stations")
    public ResponseEntity createStation(@RequestBody CreateStationRequestView view) {
        Station persistStation = stationRepository.save(view.toStation());
        return ResponseEntity
                .created(URI.create("/stations/" + persistStation.getId()))
                .body(StationResponseView.of(persistStation));
    }

    @GetMapping("/stations/{id}")
    public ResponseEntity retrieveStation(@PathVariable Long id) {
        return stationRepository.findById(id)
                .map(it -> ResponseEntity.ok().body(StationResponseView.of(it)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/stations")
    public ResponseEntity showStation() {
        List<Station> persistStations = stationRepository.findAll();
        return ResponseEntity.ok().body(StationResponseView.listOf(persistStations));
    }

    @DeleteMapping("/stations/{id}")
    public ResponseEntity deleteStation(@PathVariable Long id) {
        stationRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}

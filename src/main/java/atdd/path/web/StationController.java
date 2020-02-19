package atdd.path.web;

import atdd.path.application.exception.NoDataException;
import atdd.path.domain.Station;
import atdd.path.application.dto.CreateStationRequestView;
import atdd.path.application.dto.StationResponseView;
import atdd.path.repository.StationRepository;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.ArrayList;
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
        try {
            Station persistStation = stationRepository.findById(id).orElseThrow(NoDataException::new);
            return ResponseEntity.ok().body(StationResponseView.of(persistStation));
        } catch (EmptyResultDataAccessException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/stations")
    public ResponseEntity showStation() {
        List<Station> persistStations = new ArrayList<Station>();
        stationRepository.findAll().forEach(persistStations::add);
        return ResponseEntity.ok().body(StationResponseView.listOf(persistStations));
    }

    @DeleteMapping("/stations/{id}")
    public ResponseEntity deleteStation(@PathVariable Long id) {
        stationRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}

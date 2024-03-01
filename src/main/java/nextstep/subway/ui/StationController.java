package nextstep.subway.ui;

import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import nextstep.subway.application.dto.request.StationCreateRequest;
import nextstep.subway.application.dto.response.StationResponse;
import nextstep.subway.application.service.StationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/stations")
@RestController
@RequiredArgsConstructor
public class StationController {

    private final StationService stationService;


    @PostMapping
    public ResponseEntity<StationResponse> createStation(@RequestBody StationCreateRequest stationCreateRequest) {
        StationResponse station = stationService.saveStation(stationCreateRequest);
        return ResponseEntity.created(URI.create("/stations/" + station.getId())).body(station);
    }

    @GetMapping
    public ResponseEntity<List<StationResponse>> showStations() {
        return ResponseEntity.ok().body(stationService.findAllStations());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStation(@PathVariable Long id) {
        stationService.deleteStationById(id);
        return ResponseEntity.noContent().build();
    }
}

package nextstep.api.subway.interfaces.controller;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import nextstep.api.subway.domain.dto.inport.StationCreateCommand;
import nextstep.api.subway.domain.service.impl.StationService;
import nextstep.api.subway.interfaces.dto.request.StationCreateRequest;
import nextstep.api.subway.interfaces.dto.response.StationResponse;

@RestController
@RequiredArgsConstructor
public class StationController {
    private final StationService stationService;

    @PostMapping("/stations")
    public ResponseEntity<StationResponse> createStation(@RequestBody StationCreateRequest stationCreateRequest) {
        StationResponse station = stationService.saveStation(StationCreateCommand.from(stationCreateRequest));
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

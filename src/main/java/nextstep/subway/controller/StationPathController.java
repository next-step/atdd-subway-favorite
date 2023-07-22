package nextstep.subway.controller;

import lombok.RequiredArgsConstructor;
import nextstep.subway.service.StationPathService;
import nextstep.subway.service.dto.StationPathResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/paths")
public class StationPathController {
    private final StationPathService stationPathService;

    @GetMapping
    public ResponseEntity<StationPathResponse> getStationPath(
            @RequestParam("source") Long startStationId,
            @RequestParam("target") Long destinationStationId) {
        final StationPathResponse response = stationPathService.searchStationPath(startStationId, destinationStationId);

        return ResponseEntity.ok(response);
    }
}

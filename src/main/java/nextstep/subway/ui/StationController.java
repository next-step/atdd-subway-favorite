package nextstep.subway.ui;

import nextstep.subway.application.dto.StationRequest;
import nextstep.subway.application.dto.StationResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StationController {

    @PostMapping("/stations")
    public ResponseEntity<StationResponse> createStation(@RequestBody StationRequest stationRequest) {
        return ResponseEntity.ok().build();
    }
}
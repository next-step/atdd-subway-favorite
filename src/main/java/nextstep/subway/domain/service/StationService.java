package nextstep.subway.domain.service;

import java.util.List;

import nextstep.subway.application.dto.StationRequest;
import nextstep.subway.application.dto.StationResponse;

public interface StationService {
    StationResponse saveStation(StationRequest stationRequest);

    List<StationResponse> findAllStations();

    void deleteStationById(Long id);
}

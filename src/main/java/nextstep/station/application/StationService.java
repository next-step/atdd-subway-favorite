package nextstep.station.application;

import nextstep.station.application.dto.StationRequest;
import nextstep.station.application.dto.StationResponse;

import java.util.List;

public interface StationService {
    StationResponse saveStation(StationRequest stationRequest);

    List<StationResponse> findAllStations();

    void deleteStationById(Long id);
}

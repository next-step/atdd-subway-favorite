package nextstep.subway.domain.service;

import java.util.List;
import java.util.Optional;

import nextstep.subway.application.dto.StationRequest;
import nextstep.subway.application.dto.StationResponse;
import nextstep.subway.domain.model.Station;

public interface StationService {
    StationResponse saveStation(StationRequest stationRequest);

    List<StationResponse> findAllStations();

    Station findStationById(Long id);

    void deleteStationById(Long id);
}

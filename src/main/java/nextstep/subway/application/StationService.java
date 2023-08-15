package nextstep.subway.application;

import nextstep.subway.application.dto.station.StationRequest;
import nextstep.subway.application.dto.station.StationResponse;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.StationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class StationService {

    private StationRepository stationRepository;

    public StationService(StationRepository stationRepository) { this.stationRepository = stationRepository; }

    @Transactional
    public StationResponse createStation(StationRequest request) {
        Station station = new Station(request.getName());
        stationRepository.save(station);
        return createStationResponse(station);
    }

    private StationResponse createStationResponse(Station station) {
        return new StationResponse(station.getId(), station.getName());
    }
}
package nextstep.api.subway.applicaion.station;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import nextstep.api.subway.applicaion.station.dto.StationRequest;
import nextstep.api.subway.applicaion.station.dto.StationResponse;
import nextstep.api.subway.domain.station.Station;
import nextstep.api.subway.domain.station.StationRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StationService {
    private final StationRepository stationRepository;

    @Transactional
    public StationResponse saveStation(final StationRequest request) {
        final var station = stationRepository.save(new Station(request.getName()));
        return StationResponse.toResponse(station);
    }

    public List<StationResponse> findAllStations() {
        final var stations = stationRepository.findAll();
        return StationResponse.toResponses(stations);
    }

    @Transactional
    public void deleteStationById(Long id) {
        stationRepository.deleteById(id);
    }
}

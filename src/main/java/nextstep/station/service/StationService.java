package nextstep.station.service;

import nextstep.path.repository.PathRepository;
import nextstep.station.domain.Station;
import nextstep.station.payload.StationRequest;
import nextstep.station.payload.StationResponse;
import nextstep.station.repository.StationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class StationService {
    private final StationRepository stationRepository;
    private final PathRepository pathRepository;


    public StationService(final StationRepository stationRepository, final PathRepository pathRepository) {
        this.stationRepository = stationRepository;
        this.pathRepository = pathRepository;
    }

    @Transactional
    public StationResponse saveStation(StationRequest stationRequest) {
        pathRepository.removeAll();
        Station station = stationRepository.save(new Station(stationRequest.getName()));
        return createStationResponse(station);
    }

    public List<StationResponse> findAllStations() {
        return stationRepository.findAll().stream()
                .map(this::createStationResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteStationById(Long id) {
        pathRepository.removeAll();
        stationRepository.deleteById(id);
    }

    private StationResponse createStationResponse(Station station) {
        return StationResponse.from(station);
    }
}
